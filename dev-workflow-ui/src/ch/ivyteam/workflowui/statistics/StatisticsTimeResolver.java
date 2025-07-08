package ch.ivyteam.workflowui.statistics;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class StatisticsTimeResolver {

  public static class TimeResolution {
    public final String bucketType;
    public final int dataPoints;
    public final DateTimeFormatter labelFormatter;

    public TimeResolution(String bucketType, int dataPoints, String labelPattern) {
      this.bucketType = bucketType;
      this.dataPoints = dataPoints;
      this.labelFormatter = DateTimeFormatter.ofPattern(labelPattern).withZone(ZoneId.systemDefault());
    }
  }

  public static TimeResolution getResolutionForDuration(String timeDuration) {
    return switch (timeDuration) {
      case "today" -> new TimeResolution("hour", 24, "HH:00");
      case "24h" -> new TimeResolution("hour", 24, "HH:00");
      case "7d/d" -> new TimeResolution("day", 7, "MM-dd");
      case "30d/d" -> new TimeResolution("day", 30, "MM-dd");
      case "90d/d" -> new TimeResolution("day", 13, "MM-dd");
      case "365d/d" -> new TimeResolution("month", 12, "MMM");
      default -> new TimeResolution("month", 12, "MMM yyyy");
    };
  }

  public static String buildTimeQuery(String timeDuration) {
    return switch (timeDuration) {
      case "all" -> null;
      case "today" -> "startTimestamp:>=now/d";
      case "24h" -> "startTimestamp:>=now-24h";
      case null -> null;
      default -> "startTimestamp:>=now-" + timeDuration;
    };
  }

  public static String buildCombinedQuery(String baseQuery, String timeDuration) {
    String timeQuery = buildTimeQuery(timeDuration);
    if (timeQuery == null) {
      return baseQuery;
    }
    if (baseQuery == null || baseQuery.isEmpty()) {
      return timeQuery;
    }
    return baseQuery + " AND " + timeQuery;
  }

  public static LinkedHashMap<String, Long> initializeTimeMap(String timeDuration, TimeResolution resolution) {
    var currentTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

    if ("today".equals(timeDuration)) {
      return initializeTodayMap(currentTime, resolution.labelFormatter);
    }

    return initializeStandardMap(timeDuration, resolution, currentTime);
  }

  private static LinkedHashMap<String, Long> initializeTodayMap(ZonedDateTime currentTime, DateTimeFormatter formatter) {
    var timeCountMap = new LinkedHashMap<String, Long>();
    var startOfDay = currentTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
    var currentHour = currentTime.getHour();

    for (int i = 0; i <= currentHour; i++) {
      var timePoint = startOfDay.plusHours(i);
      var timeLabel = formatter.format(timePoint);
      timeCountMap.put(timeLabel, 0L);
    }
    return timeCountMap;
  }

  private static LinkedHashMap<String, Long> initializeStandardMap(String timeDuration, TimeResolution resolution, ZonedDateTime currentTime) {
    var timeCountMap = new LinkedHashMap<String, Long>();

    currentTime = switch (resolution.bucketType) {
      case "hour" -> currentTime.withMinute(0).withSecond(0).withNano(0);
      case "day" -> currentTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
      case "month" -> currentTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
      default -> currentTime;
    };

    for (int i = resolution.dataPoints - 1; i >= 0; i--) {
      var timePoint = switch (resolution.bucketType) {
        case "hour" -> currentTime.minusHours(i);
        case "day" -> {
          if ("90d/d".equals(timeDuration)) {
            yield currentTime.minusDays(i * 7);
          } else {
            yield currentTime.minusDays(i);
          }
        }
        case "month" -> currentTime.minusMonths(i);
        default -> currentTime.minusDays(i);
      };
      var timeLabel = resolution.labelFormatter.format(timePoint);
      timeCountMap.put(timeLabel, 0L);
    }
    return timeCountMap;
  }
}
