package ch.ivyteam.workflowui.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

public class DateUtil {
  private static final PrettyTime pretty = new PrettyTime();
  private static final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
  private static final DateTimeFormatter detailedFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

  public static String getPrettyTime(Date date) {
    if (date == null) {
      return "";
    }
    LocalDateTime dateNow = LocalDateTime.now();
    LocalDateTime dateThen = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    long hoursPassed = Duration.between(dateThen, dateNow).toHours();
    if (hoursPassed < 24) {
      return pretty.format(date);
    }
    return dateThen.format(defaultFormatter).toString();
  }

  public static String getDateAndTime(Date date) {
    return formatDate(date, defaultFormatter);
  }

  public static String getDetailed(Date date) {
    return formatDate(date, detailedFormatter);
  }

  private static String formatDate(Date date, DateTimeFormatter format)
  {
    if (date == null) {
      return "";
    }
    LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    return localDate.format(format).toString();
  }
}
