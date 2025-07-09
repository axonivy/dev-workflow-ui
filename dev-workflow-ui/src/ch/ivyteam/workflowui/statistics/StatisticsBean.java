package ch.ivyteam.workflowui.statistics;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import ch.ivyteam.ivy.searchengine.client.agg.AggregationResult;
import ch.ivyteam.ivy.searchengine.client.agg.Bucket;
import ch.ivyteam.ivy.searchengine.client.agg.Buckets;
import ch.ivyteam.ivy.workflow.caze.CaseBusinessState;
import ch.ivyteam.ivy.workflow.stats.WorkflowStats;
import ch.ivyteam.ivy.workflow.task.TaskBusinessState;

@ManagedBean
@ViewScoped
public class StatisticsBean {

  private String timeDuration = TimeDuration.LAST_24H;
  private String chartResolution = StatisticsTimeResolver.getDefaultResolution(TimeDuration.LAST_24H);

  public String getTimeFilter() {
    return timeDuration;
  }

  public String getTimeDuration() {
    return timeDuration;
  }

  public void setTimeDuration(String timeDuration) {
    this.timeDuration = timeDuration;
    this.chartResolution = StatisticsTimeResolver.getDefaultResolution(timeDuration);
  }

  public String getChartResolution() {
    return chartResolution;
  }

  public void setChartResolution(String chartResolution) {
    this.chartResolution = chartResolution;
  }

  public String[] getValidResolutions() {
    return StatisticsTimeResolver.getValidResolutions(timeDuration);
  }

  public boolean isResolutionDropdownVisible() {
    return getValidResolutions().length > 1;
  }

  public LineChartModel getTasksPerHourChart() {
    return getTasksOverTimeChart();
  }

  public LineChartModel getCasesPerDayChart() {
    return getCasesOverTimeChart();
  }

  public LineChartModel getTasksOverTimeChart() {
    var resolution = StatisticsTimeResolver.getResolutionForDurationAndType(timeDuration, chartResolution);
    var searchBucketType = switch (resolution.bucketType) {
      case Resolution.WEEK -> Resolution.DAY;
      case Resolution.HOUR6 -> Resolution.HOUR;
      default -> resolution.bucketType;
    };
    var aggrResult = WorkflowStats.current().task().aggregate(
        "startTimestamp:bucket:" + searchBucketType + ",endTimestamp:bucket:" + searchBucketType,
        StatisticsTimeResolver.buildTimeQuery(timeDuration));
    var startCountMap = StatisticsTimeResolver.initializeTimeMap(timeDuration, resolution);
    var endCountMap = StatisticsTimeResolver.initializeTimeMap(timeDuration, resolution);

    for (var agg : aggrResult.aggs()) {
      if (agg instanceof Buckets buckets) {
        for (Bucket bucket : buckets.buckets()) {
          processBucket(bucket, startCountMap, resolution.labelFormatter);
          processNestedBuckets(bucket, endCountMap, resolution.labelFormatter);
        }
      }
    }
    return createStartAndFinishLineChart(startCountMap, endCountMap);
  }

  public LineChartModel getCasesOverTimeChart() {
    var resolution = StatisticsTimeResolver.getResolutionForDurationAndType(timeDuration, chartResolution);
    var searchBucketType = switch (resolution.bucketType) {
      case Resolution.WEEK -> Resolution.DAY;
      case Resolution.HOUR6 -> Resolution.HOUR;
      default -> resolution.bucketType;
    };
    var aggrResult = WorkflowStats.current().caze().aggregate(
        "startTimestamp:bucket:" + searchBucketType + ",endTimestamp:bucket:" + searchBucketType,
        StatisticsTimeResolver.buildTimeQuery(timeDuration));
    var startCountMap = StatisticsTimeResolver.initializeTimeMap(timeDuration, resolution);
    var endCountMap = StatisticsTimeResolver.initializeTimeMap(timeDuration, resolution);

    for (var agg : aggrResult.aggs()) {
      if (agg instanceof Buckets buckets) {
        for (Bucket bucket : buckets.buckets()) {
          processBucket(bucket, startCountMap, resolution.labelFormatter);
          processNestedBuckets(bucket, endCountMap, resolution.labelFormatter);
        }
      }
    }
    return createStartAndFinishLineChart(startCountMap, endCountMap, "rgb(255, 159, 64)");
  }

  public long getAllTasks() {
    var allTasks = WorkflowStats.current().task().aggregate("businessState", StatisticsTimeResolver.buildTimeQuery(timeDuration));
    return getCountFromAggregation(allTasks);
  }

  public long getAllCases() {
    var allCases = WorkflowStats.current().caze().aggregate("businessState", StatisticsTimeResolver.buildTimeQuery(timeDuration));
    return getCountFromAggregation(allCases);
  }

  public DonutChartModel getTaskByStateGraph() {
    Map<String, String> labelToColor = Map.of(TaskBusinessState.OPEN.toString(), "rgb(0, 148, 210)",
        TaskBusinessState.IN_PROGRESS.toString(), "rgb(255, 206, 86)", TaskBusinessState.DONE.toString(),
        "rgb(54, 199, 38)", TaskBusinessState.DELAYED.toString(), "rgb(200, 200, 200)",
        TaskBusinessState.DESTROYED.toString(), "rgb(130, 130, 130)", TaskBusinessState.ERROR.toString(),
        "rgb(255, 99, 132)");
    var aggrResult = WorkflowStats.current().task().aggregate("businessState", StatisticsTimeResolver.buildTimeQuery(timeDuration));
    return createDonutChartModel(aggrResult, labelToColor);
  }

  public DonutChartModel getCaseByStateGraph() {
    Map<String, String> labelToColor = Map.of(CaseBusinessState.OPEN.toString(), "rgb(0, 148, 210)",
        CaseBusinessState.DONE.toString(), "rgb(54, 199, 38)", CaseBusinessState.DESTROYED.toString(),
        "rgb(130, 130, 130)");
    var aggResult = WorkflowStats.current().caze().aggregate("businessState", StatisticsTimeResolver.buildTimeQuery(timeDuration));
    return createDonutChartModel(aggResult, labelToColor);
  }

  public BarChartModel getTopCaseCreatorsModel() {
    var aggrResult = WorkflowStats.current().caze().aggregate("creator.name", StatisticsTimeResolver.buildTimeQuery(timeDuration));
    return createBarChartModel(aggrResult, "Cases created", "rgb(255, 159, 64)");
  }

  public BarChartModel getTopTaskWorkersModel() {
    var aggrResult = WorkflowStats.current().task().aggregate("worker.name", StatisticsTimeResolver.buildCombinedQuery("businessState:DONE", timeDuration));
    return createBarChartModel(aggrResult, "Tasks completed", "rgb(0, 148, 210)");
  }

  private LineChartModel createStartAndFinishLineChart(Map<String, Long> startCountMap, Map<String, Long> endCountMap,
      String... colors) {
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

  private void processBucket(Bucket bucket, Map<String, Long> timeCountMap, DateTimeFormatter labelFormatter) {
    updateCountMap(bucket, timeCountMap, labelFormatter);
  }

  private void processNestedBuckets(Bucket startBucket, Map<String, Long> timeCountMap,
      DateTimeFormatter labelFormatter) {
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
      var bucketTime = dateTime.atZone(ZoneId.systemDefault());

      if (Resolution.HOUR6.equals(chartResolution)) {
        var adjustedTime = bucketTime.withHour((bucketTime.getHour() / 6) * 6).withMinute(0);
        var timeLabel = labelFormatter.format(adjustedTime);
        timeCountMap.merge(timeLabel, bucket.count(), Long::sum);
      } else {
        var timeLabel = labelFormatter.format(bucketTime);
        timeCountMap.merge(timeLabel, bucket.count(), Long::sum);
      }
    } catch (Exception e) {
      System.err.println("Failed to parse date: " + bucket.key());
    }
  }

  private LineChartModel createLineChart(ChartData data) {
    var model = new LineChartModel();
    model.setData(data);
    return model;
  }

  private long getCountFromAggregation(AggregationResult aggrResult) {
    return aggrResult.aggs().stream().filter(Buckets.class::isInstance).map(aggregation -> (Buckets) aggregation)
        .flatMap(buckets -> buckets.buckets().stream()).map(Bucket::count).findFirst().orElse(0l);
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
