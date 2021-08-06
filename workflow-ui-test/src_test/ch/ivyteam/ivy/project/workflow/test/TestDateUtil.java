package ch.ivyteam.ivy.project.workflow.test;

import static ch.ivyteam.workflowui.util.DateUtil.getPrettyTime;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class TestDateUtil {

  @Test
  public void loginAndRedirect() {
    LocalDateTime date = LocalDateTime.now().minusSeconds(10);
    String prettyTime = getPrettyTime(localDateTimeToDate(date));
    assertThat(prettyTime).isEqualTo("moments ago");

    date = date.minusMinutes(6);
    prettyTime = getPrettyTime(localDateTimeToDate(date));
    assertThat(prettyTime).isEqualTo("6 minutes ago");

    date = date.minusHours(2);
    prettyTime = getPrettyTime(localDateTimeToDate(date));
    assertThat(prettyTime).isEqualTo("2 hours ago");

    date = date.minusDays(1);
    prettyTime = getPrettyTime(localDateTimeToDate(date));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");
    assertThat(prettyTime).isEqualTo(date.format(formatter));

  }

  private Date localDateTimeToDate(LocalDateTime date) {
    return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
  }

}
