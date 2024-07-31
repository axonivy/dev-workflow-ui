package ch.ivyteam.workflowui;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;

import ch.ivyteam.ivy.elasticsearch.client.agg.AggregationResult;
import ch.ivyteam.ivy.elasticsearch.client.agg.Bucket;
import ch.ivyteam.ivy.elasticsearch.client.agg.Buckets;
import ch.ivyteam.ivy.workflow.caze.CaseBusinessState;
import ch.ivyteam.ivy.workflow.stats.WorkflowStats;
import ch.ivyteam.ivy.workflow.task.TaskBusinessState;

@ManagedBean
@ViewScoped
public class StatisticsBean {

  public LineChartModel getTasksPerHourChart() {
    var aggrResult = WorkflowStats.current().task().aggregate("startTimestamp:bucket:hour,endTimestamp:bucket:hour");
    var startCountMap = initializeTimeMap(12, true);
    var endCountMap = initializeTimeMap(12, true);
    for (var agg : aggrResult.aggs()) {
      if (agg instanceof Buckets buckets) {
        for (Bucket bucket : buckets.buckets()) {
          processHourBucket(bucket, startCountMap);
          processNestedHourBuckets(bucket, endCountMap);
        }
      }
    }
    return createStartAndFinishLineChart(startCountMap, endCountMap);
  }

  private LineChartModel createStartAndFinishLineChart(Map<String, Long> startCountMap, Map<String, Long> endCountMap, String... colors) {
    var color = colors.length > 0 ? colors[0] : "rgb(54, 162, 235)";
    var startDataSet = createLineChartDataSet(new ArrayList<>(startCountMap.values()), "Started", color);
    color = colors.length > 1 ? colors[1] : "rgb(255, 99, 132)";
    var endDataSet = createLineChartDataSet(new ArrayList<>(endCountMap.values()), "Finished", color);

    var data = new ChartData();
    data.addChartDataSet(startDataSet);
    data.addChartDataSet(endDataSet);
    data.setLabels(new ArrayList<>(startCountMap.keySet()));
    return createLineChart(data);
  }

  private LineChartDataSet createLineChartDataSet(List<Object> values, String label, String backgroundColor) {
    var dataSet = new LineChartDataSet();
    dataSet.setData(values);
    dataSet.setLabel(label);
    dataSet.setBackgroundColor(backgroundColor);
    dataSet.setBorderColor(backgroundColor.replace("rgb", "rgba").replace(")", ", 0.5)"));
    return dataSet;
  }

  private void processNestedHourBuckets(Bucket startBucket, Map<String, Long> timeCountMap) {
    var labelFormatter = DateTimeFormatter.ofPattern("HH:00").withZone(ZoneId.systemDefault());
    for (var agg : startBucket.aggs()) {
      if (agg instanceof Buckets buckets) {
        for (Bucket endBucket : buckets.buckets()) {
          updateCountMap(endBucket, timeCountMap, labelFormatter);
        }
      }
    }
  }

  private void updateCountMap(Bucket bucket, Map<String, Long> timeCountMap, DateTimeFormatter labelFormatter) {
    var inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
    try {
      var dateTime = LocalDateTime.parse(bucket.key().toString(), inputFormatter);
      var bucketTime = dateTime.atZone(ZoneId.systemDefault()).toInstant();
      var timeLabel = labelFormatter.format(bucketTime);
      timeCountMap.merge(timeLabel, bucket.count(), Long::sum);
    } catch (Exception e) {
      System.err.println("Failed to parse date: " + bucket.key());
    }
  }

  public LineChartModel getCasesPerDayChart() {
    var casesStartedAggr = WorkflowStats.current().caze().aggregate("startTimestamp:bucket:day,endTimestamp:bucket:day");
    var startCountMap = initializeTimeMap(7, false);
    var endCountMap = initializeTimeMap(7, false);
    for (var agg : casesStartedAggr.aggs()) {
      if (agg instanceof Buckets buckets) {
        for (Bucket bucket : buckets.buckets()) {
          processDayBucket(bucket, startCountMap);
          processNestedDayBuckets(bucket, endCountMap);
        }
      }
    }
    return createStartAndFinishLineChart(startCountMap, endCountMap, "rgb(255, 159, 64)");
  }

  private LinkedHashMap<String, Long> initializeTimeMap(int timeRange, boolean isHour) {
    var timeCountMap = new LinkedHashMap<String, Long>();
    var currentTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).withMinute(0);
    var formatter = isHour ? DateTimeFormatter.ofPattern("HH:00") : DateTimeFormatter.ofPattern("MM-dd");
    for (int i = timeRange; i > 0; i--) {
      var timeLabel = formatter.format(isHour ? currentTime.minusHours(i) : currentTime.minusDays(i));
      timeCountMap.put(timeLabel, 0L);
    }
    return timeCountMap;
  }

  private void processHourBucket(Bucket bucket, Map<String, Long> timeCountMap) {
    var labelFormatter = DateTimeFormatter.ofPattern("HH:00").withZone(ZoneId.systemDefault());
    updateCountMap(bucket, timeCountMap, labelFormatter);
  }

  private void processNestedDayBuckets(Bucket startBucket, Map<String, Long> timeCountMap) {
    var labelFormatter = DateTimeFormatter.ofPattern("MM-dd").withZone(ZoneId.systemDefault());
    for (var agg : startBucket.aggs()) {
      if (agg instanceof Buckets buckets) {
        for (Bucket endBucket : buckets.buckets()) {
          updateCountMap(endBucket, timeCountMap, labelFormatter);
        }
      }
    }
  }

  private void processDayBucket(Bucket bucket, Map<String, Long> timeCountMap) {
    var labelFormatter = DateTimeFormatter.ofPattern("MM-dd").withZone(ZoneId.systemDefault());
    updateCountMap(bucket, timeCountMap, labelFormatter);
  }

  private LineChartModel createLineChart(ChartData data) {
    var model = new LineChartModel();
    model.setData(data);
    return model;
  }

  public long getAllTasks() {
    var allTasks = WorkflowStats.current().task().aggregate("businessState");
    return getCountFromAggregation(allTasks);
  }

  public long getAllCases() {
    var allCases = WorkflowStats.current().caze().aggregate("businessState");
    return getCountFromAggregation(allCases);
  }

  private long getCountFromAggregation(AggregationResult aggrResult) {
    return aggrResult.aggs().stream()
            .filter(aggregation -> aggregation instanceof Buckets)
            .map(aggregation -> (Buckets) aggregation)
            .flatMap(buckets -> buckets.buckets().stream())
            .map(Bucket::count)
            .findFirst()
            .orElse(0l);
  }

  public DonutChartModel getTaskByStateGraph() {
    Map<String, String> labelToColor = Map.of(
            TaskBusinessState.OPEN.toString(), "rgb(0, 148, 210)",
            TaskBusinessState.IN_PROGRESS.toString(), "rgb(255, 206, 86)",
            TaskBusinessState.DONE.toString(), "rgb(54, 199, 38)",
            TaskBusinessState.DELAYED.toString(), "rgb(200, 200, 200)",
            TaskBusinessState.DESTROYED.toString(), "rgb(130, 130, 130)",
            TaskBusinessState.ERROR.toString(), "rgb(255, 99, 132)");
    var aggrResult = WorkflowStats.current().task().aggregate("businessState");
    return createDonutChartModel(aggrResult, labelToColor);
  }

  public DonutChartModel getCaseByStateGraph() {
    Map<String, String> labelToColor = Map.of(
            CaseBusinessState.OPEN.toString(), "rgb(0, 148, 210)",
            CaseBusinessState.DONE.toString(), "rgb(54, 199, 38)",
            CaseBusinessState.DESTROYED.toString(), "rgb(130, 130, 130)");
    var aggResult = WorkflowStats.current().caze().aggregate("businessState");
    return createDonutChartModel(aggResult, labelToColor);
  }

  private DonutChartModel createDonutChartModel(AggregationResult aggrResult, Map<String, String> labelToColor) {
    var donutModel = new DonutChartModel();
    if (aggrResult.aggs().isEmpty()) {
      return null;
    }
    List<Number> values = new ArrayList<>();
    List<String> labels = new ArrayList<>();
    List<String> bgColors = new ArrayList<>();
    for (var agg : aggrResult.aggs()) {
      if (agg instanceof Buckets buckets) {
        for (Bucket bucket : buckets.buckets()) {
          String key = bucket.key().toString();
          if (labelToColor.containsKey(key)) {
            values.add(bucket.count());
            labels.add(key);
            bgColors.add(labelToColor.get(key));
          }
        }
      }
    }
    DonutChartDataSet dataSet = new DonutChartDataSet();
    dataSet.setData(values);
    dataSet.setBackgroundColor(bgColors);
    ChartData data = new ChartData();
    data.addChartDataSet(dataSet);
    data.setLabels(labels);
    donutModel.setData(data);
    return donutModel;
  }

  public BarChartModel getTopCaseCreatorsModel() {
    var aggrResult = WorkflowStats.current().caze().aggregate("creator.name");
    return createBarChartModel(aggrResult, "Cases created", "rgb(255, 159, 64)");
  }

  public BarChartModel getTopTaskWorkersModel() {
    var aggrResult = WorkflowStats.current().task().aggregate("worker.name", "businessState:DONE");
    return createBarChartModel(aggrResult, "Tasks finished", "rgb(0, 148, 210)");
  }

  private BarChartModel createBarChartModel(AggregationResult agg, String title, String color) {
    var barModel = new BarChartModel();
    if (agg.aggs().isEmpty()) {
      return null;
    }
    List<Number> values = new ArrayList<>();
    List<String> labels = new ArrayList<>();
    for (var aggr : agg.aggs()) {
      if (aggr instanceof Buckets buckets) {
        for (Bucket bucket : buckets.buckets()) {
          labels.add(cleanupUsername(bucket.key().toString()));
          values.add(bucket.count());
        }
      }
    }
    var dataSet = new BarChartDataSet();
    dataSet.setData(values);
    dataSet.setLabel(title);
    dataSet.setBorderColor(color);
    dataSet.setBorderWidth(2);
    dataSet.setBackgroundColor(color.replace("rgb", "rgba").replace(")", ", 0.5)"));
    var data = new ChartData();
    data.addChartDataSet(dataSet);
    data.setLabels(labels);
    barModel.setData(data);
    return barModel;
  }

  private static String cleanupUsername(String username) {
    return username.startsWith("#") ? username.substring(1) : username;
  }

}
