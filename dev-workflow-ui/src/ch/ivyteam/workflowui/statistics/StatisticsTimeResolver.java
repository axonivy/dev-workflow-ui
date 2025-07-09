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
      case TimeDuration.TODAY -> new TimeResolution(Resolution.HOUR, 24, "HH:00");
      case TimeDuration.LAST_24H -> new TimeResolution(Resolution.HOUR, 24, "HH:00");
      case TimeDuration.LAST_7D -> new TimeResolution(Resolution.DAY, 7, "MM-dd");
      case TimeDuration.LAST_30D -> new TimeResolution(Resolution.DAY, 30, "MM-dd");
      case TimeDuration.LAST_90D -> new TimeResolution(Resolution.DAY, 13, "MM-dd");
      case TimeDuration.LAST_365D -> new TimeResolution(Resolution.MONTH, 12, "MMM");
      default -> new TimeResolution(Resolution.MONTH, 12, "MMM yyyy");
    };
  }

  public static TimeResolution getResolutionForDurationAndType(String timeDuration, String resolutionType) {
    return switch (resolutionType) {
      case Resolution.HOUR -> new TimeResolution(Resolution.HOUR, calculateHourDataPoints(timeDuration), "HH:00");
      case Resolution.HOUR6 -> new TimeResolution(Resolution.HOUR6, calculate6HourDataPoints(timeDuration), "MM-dd HH:00");
      case Resolution.DAY -> new TimeResolution(Resolution.DAY, calculateDayDataPoints(timeDuration), "MM-dd");
      case Resolution.WEEK -> new TimeResolution(Resolution.WEEK, calculateWeekDataPoints(timeDuration), "MM-dd");
      case Resolution.MONTH -> new TimeResolution(Resolution.MONTH, calculateMonthDataPoints(timeDuration), "MMM");
      default -> getResolutionForDuration(timeDuration);
    };
  }

  public static String[] getValidResolutions(String timeDuration) {
    return switch (timeDuration) {
      case TimeDuration.TODAY, TimeDuration.LAST_24H -> new String[]{Resolution.HOUR};
      case TimeDuration.LAST_7D -> new String[]{Resolution.HOUR6, Resolution.DAY};
      case TimeDuration.LAST_30D -> new String[]{Resolution.DAY, Resolution.WEEK};
      case TimeDuration.LAST_90D -> new String[]{Resolution.DAY, Resolution.WEEK};
      case TimeDuration.LAST_365D -> new String[]{Resolution.WEEK, Resolution.MONTH};
      default -> new String[]{Resolution.MONTH};
    };
  }

  public static String getDefaultResolution(String timeDuration) {
    return switch (timeDuration) {
      case TimeDuration.TODAY, TimeDuration.LAST_24H -> Resolution.HOUR;
      case TimeDuration.LAST_7D -> Resolution.HOUR6;
      case TimeDuration.LAST_30D -> Resolution.DAY;
      case TimeDuration.LAST_90D -> Resolution.WEEK;
      case TimeDuration.LAST_365D -> Resolution.MONTH;
      default -> Resolution.MONTH;
    };
  }

  private static int calculateHourDataPoints(String timeDuration) {
    return switch (timeDuration) {
      case TimeDuration.TODAY, TimeDuration.LAST_24H -> 24;
      default -> 24;
    };
  }

  private static int calculate6HourDataPoints(String timeDuration) {
    return switch (timeDuration) {
      case TimeDuration.LAST_7D -> 28;
      default -> 28;
    };
  }

  private static int calculateDayDataPoints(String timeDuration) {
    return switch (timeDuration) {
      case TimeDuration.LAST_7D -> 7;
      case TimeDuration.LAST_30D -> 30;
      case TimeDuration.LAST_90D -> 90;
      default -> 30;
    };
  }

  private static int calculateWeekDataPoints(String timeDuration) {
    return switch (timeDuration) {
      case TimeDuration.LAST_30D -> 5;
      case TimeDuration.LAST_90D -> 13;
      case TimeDuration.LAST_365D -> 53;
      default -> 4;
    };
  }

  private static int calculateMonthDataPoints(String timeDuration) {
    return switch (timeDuration) {
      case TimeDuration.LAST_365D, TimeDuration.ALL_TIME -> 12;
      default -> 12;
    };
  }

  public static String buildTimeQuery(String timeDuration) {
    return switch (timeDuration) {
      case TimeDuration.ALL_TIME -> null;
      case TimeDuration.TODAY -> "startTimestamp:>=now/d";
      case TimeDuration.LAST_24H -> "startTimestamp:>=now-24h";
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

    if (TimeDuration.TODAY.equals(timeDuration)) {
      return initializeTodayMap(currentTime, resolution.labelFormatter);
    }

    return initializeStandardMap(resolution, currentTime);
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

  private static LinkedHashMap<String, Long> initializeStandardMap(TimeResolution resolution, ZonedDateTime currentTime) {
    var timeCountMap = new LinkedHashMap<String, Long>();

    currentTime = switch (resolution.bucketType) {
      case Resolution.HOUR -> currentTime.withMinute(0).withSecond(0).withNano(0);
      case Resolution.HOUR6 -> currentTime.withMinute(0).withSecond(0).withNano(0).withHour((currentTime.getHour() / 6) * 6);
      case Resolution.DAY -> currentTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
      case Resolution.WEEK -> currentTime.withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(currentTime.getDayOfWeek().getValue() - 1);
      case Resolution.MONTH -> currentTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
      default -> currentTime;
    };

    for (int i = resolution.dataPoints - 1; i >= 0; i--) {
      var timePoint = switch (resolution.bucketType) {
        case Resolution.HOUR -> currentTime.minusHours(i);
        case Resolution.HOUR6 -> currentTime.minusHours(i * 6);
        case Resolution.DAY -> currentTime.minusDays(i);
        case Resolution.WEEK -> currentTime.minusWeeks(i);
        case Resolution.MONTH -> currentTime.minusMonths(i);
        default -> currentTime.minusDays(i);
      };
      var timeLabel = resolution.labelFormatter.format(timePoint);
      timeCountMap.put(timeLabel, 0L);
    }
    return timeCountMap;
  }
}
