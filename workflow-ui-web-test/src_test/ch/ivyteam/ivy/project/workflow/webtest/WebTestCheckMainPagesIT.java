package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlEndsWith;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import com.axonivy.ivy.webtest.IvyWebTest;

@IvyWebTest
public class WebTestCheckMainPagesIT
{

  @Test
  public void checkMainPagesContent()
  {
    open(viewUrl("home.xhtml"));
    assertCurrentUrlEndsWith("home.xhtml");
    assertThat($("p").text()).contains("Hello this is home");

    $(By.id("menuform:sr_tasks")).click();
    assertCurrentUrlEndsWith("tasks.xhtml");
    assertThat($("p").text()).contains("My tasks are here");

    $(By.id("menuform:sr_starts")).click();
    assertCurrentUrlEndsWith("starts.xhtml");
    assertThat($("p").text()).contains("This is starts");
  }

}
