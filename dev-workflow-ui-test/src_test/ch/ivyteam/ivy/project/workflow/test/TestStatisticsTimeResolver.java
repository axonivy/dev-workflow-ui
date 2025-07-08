package ch.ivyteam.ivy.project.workflow.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import ch.ivyteam.workflowui.statistics.StatisticsTimeResolver;
import ch.ivyteam.workflowui.statistics.StatisticsTimeResolver.TimeResolution;

public class TestStatisticsTimeResolver {

  @Test
  public void testGetResolutionForDuration() {
    TimeResolution todayResolution = StatisticsTimeResolver.getResolutionForDuration("today");
    assertThat(todayResolution.bucketType).isEqualTo("hour");
    assertThat(todayResolution.dataPoints).isEqualTo(24);
    assertThat(todayResolution.labelFormatter.format(ZonedDateTime.now().withHour(14).withMinute(0))).isEqualTo("14:00");

    TimeResolution twentyFourHourResolution = StatisticsTimeResolver.getResolutionForDuration("24h");
    assertThat(twentyFourHourResolution.bucketType).isEqualTo("hour");
    assertThat(twentyFourHourResolution.dataPoints).isEqualTo(24);

    TimeResolution weekResolution = StatisticsTimeResolver.getResolutionForDuration("7d/d");
    assertThat(weekResolution.bucketType).isEqualTo("day");
    assertThat(weekResolution.dataPoints).isEqualTo(7);

    TimeResolution ninetyDayResolution = StatisticsTimeResolver.getResolutionForDuration("90d/d");
    assertThat(ninetyDayResolution.bucketType).isEqualTo("day");
    assertThat(ninetyDayResolution.dataPoints).isEqualTo(13);

    TimeResolution yearResolution = StatisticsTimeResolver.getResolutionForDuration("365d/d");
    assertThat(yearResolution.bucketType).isEqualTo("month");
    assertThat(yearResolution.dataPoints).isEqualTo(12);

    TimeResolution defaultResolution = StatisticsTimeResolver.getResolutionForDuration("unknown");
    assertThat(defaultResolution.bucketType).isEqualTo("month");
    assertThat(defaultResolution.dataPoints).isEqualTo(12);
  }

  @Test
  public void testBuildTimeQuery() {
    assertThat(StatisticsTimeResolver.buildTimeQuery("all")).isNull();
    assertThat(StatisticsTimeResolver.buildTimeQuery("today")).isEqualTo("startTimestamp:>=now/d");
    assertThat(StatisticsTimeResolver.buildTimeQuery("24h")).isEqualTo("startTimestamp:>=now-24h");
    assertThat(StatisticsTimeResolver.buildTimeQuery(null)).isNull();
    assertThat(StatisticsTimeResolver.buildTimeQuery("7d/d")).isEqualTo("startTimestamp:>=now-7d/d");
    assertThat(StatisticsTimeResolver.buildTimeQuery("30d/d")).isEqualTo("startTimestamp:>=now-30d/d");
  }

  @Test
  public void testBuildCombinedQuery() {
    assertThat(StatisticsTimeResolver.buildCombinedQuery("businessState:DONE", "all"))
        .isEqualTo("businessState:DONE");

    assertThat(StatisticsTimeResolver.buildCombinedQuery("", "24h"))
        .isEqualTo("startTimestamp:>=now-24h");

    assertThat(StatisticsTimeResolver.buildCombinedQuery(null, "today"))
        .isEqualTo("startTimestamp:>=now/d");

    assertThat(StatisticsTimeResolver.buildCombinedQuery("businessState:DONE", "7d/d"))
        .isEqualTo("businessState:DONE AND startTimestamp:>=now-7d/d");
  }

  @Test
  public void testInitializeTimeMapToday() {
    TimeResolution resolution = StatisticsTimeResolver.getResolutionForDuration("today");
    var timeMap = StatisticsTimeResolver.initializeTimeMap("today", resolution);

    assertThat(timeMap).isNotEmpty();

    assertThat(timeMap.keySet().iterator().next()).isEqualTo("00:00");

    assertThat(timeMap.values()).allMatch(count -> count == 0L);

    var currentHour = ZonedDateTime.now().getHour();
    assertThat(timeMap).hasSizeLessThanOrEqualTo(currentHour + 1);
  }

  @Test
  public void testInitializeTimeMapStandard() {
    TimeResolution resolution = StatisticsTimeResolver.getResolutionForDuration("7d/d");
    var timeMap = StatisticsTimeResolver.initializeTimeMap("7d/d", resolution);

    assertThat(timeMap).hasSize(7);

    assertThat(timeMap.values()).allMatch(count -> count == 0L);

    var formatter = DateTimeFormatter.ofPattern("MM-dd");
    var today = ZonedDateTime.now().format(formatter);
    assertThat(timeMap.keySet()).contains(today);
  }

  @Test
  public void testInitializeTimeMapWeeklySampling() {
    TimeResolution resolution = StatisticsTimeResolver.getResolutionForDuration("90d/d");
    var timeMap = StatisticsTimeResolver.initializeTimeMap("90d/d", resolution);

    // should contain exactly 13 entries for weekly sampling
    assertThat(timeMap).hasSize(13);

    assertThat(timeMap.values()).allMatch(count -> count == 0L);
  }

  @Test
  public void testInitializeTimeMapMonthly() {
    TimeResolution resolution = StatisticsTimeResolver.getResolutionForDuration("365d/d");
    var timeMap = StatisticsTimeResolver.initializeTimeMap("365d/d", resolution);

    assertThat(timeMap).hasSize(12);

    assertThat(timeMap.values()).allMatch(count -> count == 0L);

    var currentMonth = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MMM"));
    assertThat(timeMap.keySet()).contains(currentMonth);
  }

  @Test
  public void testTimeResolutionLabelFormatting() {
    TimeResolution hourResolution = new TimeResolution("hour", 24, "HH:mm");
    var testTime = ZonedDateTime.of(2023, 12, 15, 14, 30, 0, 0, ZoneId.systemDefault());
    assertThat(hourResolution.labelFormatter.format(testTime)).isEqualTo("14:30");

    TimeResolution dayResolution = new TimeResolution("day", 7, "MM-dd");
    assertThat(dayResolution.labelFormatter.format(testTime)).isEqualTo("12-15");

    TimeResolution monthResolution = new TimeResolution("month", 12, "MMM");
    assertThat(monthResolution.labelFormatter.format(testTime)).isEqualTo("Dec");
  }
}
