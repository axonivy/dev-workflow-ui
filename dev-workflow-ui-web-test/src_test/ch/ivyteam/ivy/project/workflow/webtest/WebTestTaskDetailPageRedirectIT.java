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
class WebTestTaskDetailPageRedirectIT {

  @BeforeAll
  static void setup() {
    loginDeveloper();
    startTestProcess("18D3AB6E2DC7779B/generateUserTask.ivp");
    openView("tasks.xhtml");
    var table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("TaskDetailTest"));
    var stateElement = $(By.id("tasksForm:tasks:0:taskState:stateBadge"));
    stateElement.shouldHave(text("open"));
    stateElement.click();
  }

  @Test
  void redirectToDetailPage() {
    Selenide.closeWebDriver();
    loginDeveloper();
    openView("tasks.xhtml");
    var stateElement = $(By.id("tasksForm:tasks:0:taskState:stateBadge"));
    assertThat(stateElement.getAttribute("class")).contains("state-open");
    stateElement.click();
    assertThat($(By.className("layout-dashboard")).getLocation() != null).isTrue();
    var url = Selenide.webdriver().driver().url();
    assertThat(url.contains("task.xhtml?id=") && !url.contains("frame.xhtml")).isTrue();
  }
}
