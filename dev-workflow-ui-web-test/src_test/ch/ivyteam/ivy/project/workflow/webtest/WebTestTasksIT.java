package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;

@IvyWebTest
public class WebTestTasksIT {

  @BeforeAll
  public static void prepare() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("home.xhtml");
  }

  @Test
  public void allTasksOnlyAdmin() {
    startTestProcess("1750C5211D94569D/HomePageTestData.ivp");
    openView("allTasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldBe(text("Created task of HomePageTestData"));
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("taskName")).shouldBe(text("Created task of HomePageTestData"));
    String taskId = $(By.id("taskId")).text();
    $(By.id("actionMenuForm:taskStartBtn")).shouldBe(enabled).click();
    loginFromTable("testuser");
    openView("allTasks.xhtml");
    table = PrimeUi.table(By.id("tasksForm:tasks"));
    if (table.row(0).text().equals("Created task of HomePageTestData")) {
      $(By.className("detail-btn")).shouldBe(visible).click();
      $(By.id("taskId")).shouldNotBe(text(taskId));
    }
  }

  @Test
  public void testTasksTable() throws Exception {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("allTasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldBe(text("Created task of TestData"));
    table.valueAt(0, 1).contains("pause");
  }

  @Test
  public void checkTaskDetails() {
    openView("allTasks.xhtml");
    $(".detail-btn").shouldBe(visible).click();

    $(".case-link").shouldHave(text("Created case of TestData"));
    $(By.id("taskResponsible:userName")).shouldBe(exactText("Everybody"));
    $(By.id("taskState")).shouldBe(exactText("SUSPENDED"));
    $(By.id("category")).shouldHave(exactText("TaskWithACategory"));
    $(By.id("pmv")).shouldBe(exactText("dev-workflow-ui-test-data$1"));
    $(By.className("si-hierarchy-6")).shouldBe(visible);

    $(By.id("workflowEvents:eventsTable:0:eventType")).shouldBe(exactText("EVENT_CREATE_TASK_BY_JOINED_TASKS"));
    $(By.id("actionMenuForm:taskActionsBtn")).click();
    $(By.id("actionMenuForm:taskParkBtn")).should(visible).click();
    $(By.id("taskState")).shouldBe(exactText("PARKED"));
    $(By.id("workingUser:userName")).shouldBe(exactText($("#sessionUserName").getText()));
    $(By.id("workflowEvents:eventsTable:0:eventType")).shouldBe(exactText("EVENT_PARK_TASK"));
  }

  @Test
  public void testStartTask() {
    openView("allTasks.xhtml");

    $(".detail-btn").shouldBe(visible).click();
    $(".case-link").shouldHave(text("Created case of TestData"));

    $("#taskState").shouldBe(exactText("SUSPENDED"));
    $("#actionMenuForm\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"))
            .shouldBe(enabled).click();
    openView("allTasks.xhtml");
    $(".detail-btn").shouldBe(visible).click();
    $("#taskName").shouldBe(exactText("Created task of TestData"));

    $("#taskState").shouldBe(exactText("DONE"));
    $("#actionMenuForm\\:taskStartBtn").shouldHave(cssClass("ui-state-disabled"));
  }

  @Test
  public void testStartParkedTask() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("allTasks.xhtml");

    $(".detail-btn").shouldBe(visible).click();
    $(".case-link").shouldHave(text("Created case of TestData"));

    $("#taskState").shouldBe(exactText("SUSPENDED"));
    $("#actionMenuForm\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"));

    $("#actionMenuForm\\:taskActionsBtn").click();
    $("#actionMenuForm\\:taskParkBtn").should(visible).click();

    $("#taskState").shouldBe(exactText("PARKED"));
    $("#workingUser\\:userName").shouldBe(exactText($("#sessionUserName").getText()));
    $("#actionMenuForm\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"));
  }

  @Test
  public void workflowEventsPermission() {
    openView("allTasks.xhtml");
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.className("case-link")).shouldHave(text("Created case of TestData"));
    var taskId = $(By.id("taskId")).getText();
    $(By.id("workflowEvents:eventsTable")).shouldBe(visible);

    loginFromTable("testuser");
    openView("allTasks.xhtml");
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("taskId")).shouldBe(exactText(taskId));
    $(By.id("workflowEvents:eventsTable")).shouldNotBe(visible);
    $(By.id("workflowEvents:noPermissionMessage")).shouldBe(visible).shouldHave(text("No permissions"));
  }

  @Test
  public void taskCustomFields() {
    openView("allTasks.xhtml");
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.className("case-link")).shouldHave(text("Created case of TestData"));

    Table fieldsTable = PrimeUi.table(By.id("customFields:customFieldsTable"));
    fieldsTable.valueAt(1, 0).contains("task test value");
  }

  @Test
  public void checkDelayedTask() {
    startTestProcess("1750C5211D94569D/DelayedTestTask.ivp");
    openView("allTasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldBe(text("Created delayed task"));
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("taskName")).shouldBe(exactText("Created delayed task"));
    $(By.id("taskState")).shouldBe(exactText("DELAYED"));
    $(By.id("delayDate")).shouldNotBe(exactText("N/A"));
    $(By.id("actionMenuForm:taskStartBtn")).shouldHave(cssClass("ui-state-disabled"));
  }

  @Test
  public void checkCustomResponsibleUser() {
    startTestProcess("1750C5211D94569D/customUser.ivp");
    openView("allTasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldBe(text("Created task of CustomUser"));
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("taskName")).shouldBe(exactText("Created task of CustomUser"));
    $(By.id("taskResponsible:userName")).shouldHave(text("CustomUserTest"));
  }

  @Test
  public void delegateTask() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("allTasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldBe(text("Created task of TestData"));
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("taskName")).shouldBe(exactText("Created task of TestData"));
    $(By.id("taskResponsible:userName")).shouldBe(exactText("Everybody"));

    $(By.id("actionMenuForm:taskActionsBtn")).click();
    $(By.id("actionMenuForm:taskDelegateBtn")).should(visible).click();
    $(By.id("delegateTaskDialog")).shouldBe(visible);
    PrimeUi.selectOne(By.id("delegateTaskForm:selectUserMenu")).selectItemByLabel("testuser");

    $(By.id("delegateTaskForm:delegateProceedButton")).click();
    $(By.id("taskResponsible:userName")).shouldHave(text("testuser"));
  }

  @Test
  public void customFielEmbedInFrame() {
    openView("starts.xhtml");
    $(By.id("startsForm:globalFilter")).sendKeys("embed in frame");
    $(By.className("start-link")).shouldBe(visible, text("Do not embed in Frame")).click();
    $(By.id("form:proceed")).shouldBe(visible).click();
    openView("allTasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldBe(text("notEmbedTask"));
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("actionMenuForm:taskStartBtn")).shouldNotHave(text("?taskUrl"));
  }
}
