package ch.ivyteam.workflowui.statistics;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.charts.DoughnutChart;
import software.xdev.chartjs.model.charts.LineChart;
import software.xdev.chartjs.model.color.RGBAColor;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.data.DoughnutData;
import software.xdev.chartjs.model.data.LineData;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.dataset.DoughnutDataset;
import software.xdev.chartjs.model.dataset.LineDataset;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.DoughnutOptions;
import software.xdev.chartjs.model.options.LineOptions;

import ch.ivyteam.ivy.searchengine.client.agg.AggregationResult;
import ch.ivyteam.ivy.searchengine.client.agg.Bucket;
import ch.ivyteam.ivy.searchengine.client.agg.Buckets;
import ch.ivyteam.ivy.workflow.caze.CaseBusinessState;
import ch.ivyteam.ivy.workflow.stats.WorkflowStats;
import ch.ivyteam.ivy.workflow.task.TaskBusinessState;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class StatisticsBean implements Serializable {

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

  public String getTasksPerHourChart() {
    return getTasksOverTimeChart();
  }

  public String getCasesPerDayChart() {
    return getCasesOverTimeChart();
  }

  public String getTasksOverTimeChart() {
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

  public String getCasesOverTimeChart() {
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

  public String getTaskByStateGraph() {
    Map<String, RGBAColor> labelToColor = Map.of(TaskBusinessState.OPEN.toString(), new RGBAColor(0, 148, 210),
        TaskBusinessState.IN_PROGRESS.toString(), new RGBAColor(255, 206, 86), TaskBusinessState.DONE.toString(),
        new RGBAColor(54, 199, 38), TaskBusinessState.DELAYED.toString(), new RGBAColor(200, 200, 200),
        TaskBusinessState.DESTROYED.toString(), new RGBAColor(130, 130, 130), TaskBusinessState.ERROR.toString(),
        new RGBAColor(255, 99, 132));
    var aggrResult = WorkflowStats.current().task().aggregate("businessState", StatisticsTimeResolver.buildTimeQuery(timeDuration));
    return createDonutChartModel(aggrResult, labelToColor);
  }

  public String getCaseByStateGraph() {
    Map<String, RGBAColor> labelToColor = Map.of(CaseBusinessState.OPEN.toString(), new RGBAColor(0, 148, 210),
        CaseBusinessState.DONE.toString(), new RGBAColor(54, 199, 38), CaseBusinessState.DESTROYED.toString(),
        new RGBAColor(130, 130, 130));
    var aggResult = WorkflowStats.current().caze().aggregate("businessState", StatisticsTimeResolver.buildTimeQuery(timeDuration));
    return createDonutChartModel(aggResult, labelToColor);
  }

  public String getTopCaseCreatorsModel() {
    var aggrResult = WorkflowStats.current().caze().aggregate("creator.name", StatisticsTimeResolver.buildTimeQuery(timeDuration));
    return createBarChartModel(aggrResult, "Cases created", "rgb(255, 159, 64)");
  }

  public String getTopTaskWorkersModel() {
    var aggrResult = WorkflowStats.current().task().aggregate("worker.name", StatisticsTimeResolver.buildCombinedQuery("businessState:DONE", timeDuration));
    return createBarChartModel(aggrResult, "Tasks completed", "rgb(0, 148, 210)");
  }

  private String createStartAndFinishLineChart(Map<String, Long> startCountMap, Map<String, Long> endCountMap,
      String... colors) {
    var color = colors.length > 0 ? colors[0] : "rgb(54, 162, 235)";
    var startDataSet = createLineChartDataSet(new ArrayList<Number>(startCountMap.values()), "Started", color);
    color = colors.length > 1 ? colors[1] : "rgb(255, 99, 132)";
    var endDataSet = createLineChartDataSet(new ArrayList<Number>(endCountMap.values()), "Finished", color);
    var data = new LineData().addDataset(startDataSet).addDataset(endDataSet)
        .setLabels(new ArrayList<>(startCountMap.keySet()));
    return createLineChart(data);
  }

  private LineDataset createLineChartDataSet(List<Number> values, String label, String color) {
    return new LineDataset().setData(values).setLabel(label).setBackgroundColor(color)
        .setBorderColor(color.replace("rgb", "rgba").replace(")", ", 0.5)"));
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
    } catch (Exception _) {
      System.err.println("Failed to parse date: " + bucket.key());
    }
  }

  private String createLineChart(LineData data) {
    return new LineChart(data, new LineOptions().setMaintainAspectRatio(Boolean.FALSE)).toJson();
  }

  private long getCountFromAggregation(AggregationResult aggrResult) {
    return aggrResult.aggs().stream()
        .filter(Buckets.class::isInstance)
        .map(Buckets.class::cast)
        .flatMap(buckets -> buckets.buckets().stream())
        .map(Bucket::count)
        .findFirst()
        .orElse(0l);
  }

  private String createDonutChartModel(AggregationResult aggrResult, Map<String, RGBAColor> labelToColor) {
    if (aggrResult.aggs().isEmpty()) {
      return null;
    }
    List<Number> values = new ArrayList<>();
    List<String> labels = new ArrayList<>();
    List<RGBAColor> backgroundColors = new ArrayList<>();
    for (var agg : aggrResult.aggs()) {
      if (agg instanceof Buckets buckets) {
        for (Bucket bucket : buckets.buckets()) {
          String key = bucket.key().toString();
          if (labelToColor.containsKey(key)) {
            values.add(bucket.count());
            labels.add(key);
            backgroundColors.add(labelToColor.get(key));
          }
        }
      }
    }
    var dataSet = new DoughnutDataset().setData(values).addBackgroundColors(backgroundColors);
    var data = new DoughnutData().addDataset(dataSet).setLabels(labels);
    return new DoughnutChart(data, new DoughnutOptions().setMaintainAspectRatio(Boolean.FALSE)).toJson();
  }

  private String createBarChartModel(AggregationResult agg, String title, String color) {
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
    var dataSet = new BarDataset().setData(values).setLabel(title).setBorderColor(color).setBorderWidth(2)
      .setBackgroundColor(color.replace("rgb", "rgba").replace(")", ", 0.5)"));
    var data = new BarData().addDataset(dataSet).setLabels(labels);
    return new BarChart(data, new BarOptions().setMaintainAspectRatio(Boolean.FALSE)).toJson();
  }

  private static String cleanupUsername(String username) {
    return username.startsWith("#") ? username.substring(1) : username;
  }
}
