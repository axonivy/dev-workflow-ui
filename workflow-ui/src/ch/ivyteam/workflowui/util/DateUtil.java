package ch.ivyteam.workflowui.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

public class DateUtil {
  private static final PrettyTime pretty = new PrettyTime();
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

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
    return dateThen.format(formatter).toString();
  }

  public static String getDateAndTime(Date date) {
    if (date == null) {
      return "";
    }
    LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    return localDate.format(formatter).toString();
  }
}
