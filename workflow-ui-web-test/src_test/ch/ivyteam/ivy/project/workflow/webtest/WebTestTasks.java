package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;


@IvyWebTest
public class WebTestTasks
{

  @Test
  public void allTasksOnlyAdmin()
  {
    Selenide.open(viewUrl("home.xhtml"));
    loginFromTable("testuser");
    $(By.id("menuform:sr_tasks_cases")).shouldNotBe(visible);
    loginFromTable("Developer");
    $(By.id("menuform:sr_tasks_cases")).shouldBe(visible);
  }

}
