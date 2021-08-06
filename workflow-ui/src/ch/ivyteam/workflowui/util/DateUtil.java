package ch.ivyteam.workflowui.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

public class DateUtil {
  public static String getPrettyTime(Date date) {
    if (date == null) {
      return "";
    }
    LocalDateTime dateNow = LocalDateTime.now();
    LocalDateTime dateThen = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    long hoursPassed = Duration.between(dateThen, dateNow).toHours();
    if (hoursPassed < 24) {
      PrettyTime pretty = new PrettyTime();
      return pretty.format(date);
    } else {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");
      return dateThen.format(formatter).toString();
    }
  }

  public static String getDateAndTime(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    return date == null ? "" : formatter.format(date);
  }
}
