package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestTaskDetailPageRedirectIT {

  @BeforeAll
  public static void setup() {
    loginDeveloper();
    startTestProcess("18D3AB6E2DC7779B/generateUserTask.ivp");
    openView("tasks.xhtml");
    var table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("TaskDetailTest"));
    var element = $(By.id("tasksForm:tasks:0:taskState"));
    assertThat(element.getAttribute("class")).contains("task-state-open");
    element.click();
  }

  @Test
  public void testRedirectToDetailPage() {
    Selenide.closeWebDriver();
    loginDeveloper();
    openView("tasks.xhtml");
    var element = $(By.id("tasksForm:tasks:0:taskState"));
    assertThat(element.getAttribute("class")).contains("task-state-in-progress");
    element.click();
    assertThat($(By.className("layout-dashboard")).getLocation() != null).isTrue();
    var url = Selenide.webdriver().driver().url();
    assertThat(url.contains("task.xhtml?id=") && !url.contains("frame.xhtml")).isTrue();
  }
}
