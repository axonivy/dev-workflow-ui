package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestTasksIT {

  @Test
  public void allTasksOnlyAdmin() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    Selenide.open(viewUrl("home.xhtml"));
    loginDeveloper();
    startTestProcess("1750C5211D94569D/HomePageTestData.ivp");
    open(viewUrl("allTasks.xhtml"));
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldBe(text("Created task of HomePageTestData"));
    $(By.className("si-information-circle")).shouldBe(visible).click();
    $(By.id("form:taskName")).shouldBe(text("Created task of HomePageTestData"));
    String taskId = $(By.id("form:taskId")).text();
    $(By.id("form:taskStartBtn")).shouldBe(enabled).click();
    loginFromTable("testuser");
    open(viewUrl("allTasks.xhtml"));
    table = PrimeUi.table(By.id("tasksForm:tasks"));
    if (table.row(0).text().equals("Created task of HomePageTestData")) {
      $(By.className("si-information-circle")).shouldBe(visible).click();
      $(By.id("form:taskId")).shouldNotBe(text(taskId));
    }
  }

  @Test
  public void testTasksTable() throws Exception {
    open(viewUrl("home.xhtml"));
    loginDeveloper();

    startTestProcess("1750C5211D94569D/TestData.ivp");
    open(viewUrl("allTasks.xhtml"));
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldBe(text("Created task of TestData"));

    table.valueAt(0, 1).contains("pause");
  }

  @Test
  public void chceckTaskDetails() {
    open(viewUrl("home.xhtml"));
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    open(viewUrl("allTasks.xhtml"));

    $(".si-information-circle").shouldBe(visible).click();
    $(".case-link").shouldHave(text("Created case of TestData"));

    $("#form\\:taskResponsible\\:userName").shouldBe(exactText("Everybody"));
    $("#form\\:taskState").shouldBe(exactText("SUSPENDED"));

    $("#form\\:taskActionsBtn").click();
    $("#form\\:taskParkBtn").should(visible).click();

    $("#form\\:taskState").shouldBe(exactText("PARKED"));
    $("#form\\:workingUser\\:userName").shouldBe(exactText($("#sessionUserName").getText()));

    $("#form\\:events\\:0\\:eventType").shouldBe(exactText("EVENT_CREATE_TASK_BY_JOINED_TASKS"));
  }

  @Test
  public void testStartTask() {
    open(viewUrl("home.xhtml"));
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    open(viewUrl("allTasks.xhtml"));

    $(".si-information-circle").shouldBe(visible).click();
    $(".case-link").shouldHave(text("Created case of TestData"));

    $("#form\\:taskState").shouldBe(exactText("SUSPENDED"));
    $("#form\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"));

    $("#form\\:taskStartBtn").shouldBe(enabled).click();
    open(viewUrl("allTasks.xhtml"));
    $(".si-information-circle").shouldBe(visible).click();
    $("#form\\:taskName").shouldBe(exactText("Created task of TestData"));

    $("#form\\:taskState").shouldBe(exactText("DONE"));
    $("#form\\:taskStartBtn").shouldHave(cssClass("ui-state-disabled"));
  }

  @Test
  public void testStartParkedTask() {
    open(viewUrl("home.xhtml"));
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    open(viewUrl("allTasks.xhtml"));

    $(".si-information-circle").shouldBe(visible).click();
    $(".case-link").shouldHave(text("Created case of TestData"));

    $("#form\\:taskState").shouldBe(exactText("SUSPENDED"));
    $("#form\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"));

    $("#form\\:taskActionsBtn").click();
    $("#form\\:taskParkBtn").should(visible).click();

    $("#form\\:taskState").shouldBe(exactText("PARKED"));
    $("#form\\:workingUser\\:userName").shouldBe(exactText($("#sessionUserName").getText()));
    $("#form\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"));
  }
}
