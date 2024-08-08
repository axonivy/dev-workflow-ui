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
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

@IvyWebTest
class WebTestTasksIT {

  @BeforeAll
  static void prepare() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("home.xhtml");
  }

  @Test
  void allTasksOnlyAdmin() {
    startTestProcess("1750C5211D94569D/HomePageTestData.ivp");
    openView("tasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("Created task of HomePageTestData"));
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("taskName")).shouldHave(text("Created task of HomePageTestData"));
    String taskId = $(By.id("taskId")).text();
    $(By.id("actionMenuForm:taskStartBtn")).shouldBe(enabled).click();
    loginFromTable("testuser");
    openView("tasks.xhtml");
    table = PrimeUi.table(By.id("tasksForm:tasks"));
    if (table.row(0).text().equals("Created task of HomePageTestData")) {
      $(By.className("detail-btn")).shouldBe(visible).click();
      $(By.id("taskId")).shouldNotBe(text(taskId));
    }
  }

  @Test
  void tasksTable() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("tasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("Created task of TestData"));
    table.valueAt(0, 1).contains("pause");
  }

  @Test
  void checkTaskDetails() {
    openView("tasks.xhtml");
    $(".detail-btn").shouldBe(visible).click();

    $(".case-link").shouldHave(text("Created case of TestData"));
    $(By.id("taskResponsible:userName")).shouldBe(exactText("Everybody"));
    $(By.id("taskState")).shouldBe(exactText("OPEN (SUSPENDED)"));
    $(By.id("category")).shouldHave(exactText("TaskWithACategory"));
    $(By.id("pmv")).shouldBe(exactText("dev-workflow-ui-test-data$1"));
    $(By.className("si-hierarchy-6")).shouldBe(visible);

    $(By.id("workflowEvents:eventsTable:0:eventType")).shouldBe(exactText("EVENT_CREATE_TASK_BY_JOINED_TASKS"));
    $(By.id("actionMenuForm:taskActionsBtn")).click();
    $(By.id("actionMenuForm:taskParkBtn")).should(visible).click();
    $(By.id("taskState")).shouldBe(exactText("OPEN (PARKED)"));
    $(By.id("workingUser:userName")).shouldBe(exactText($("#sessionUserName").getText()));
    $(By.id("workflowEvents:eventsTable:0:eventType")).shouldBe(exactText("EVENT_PARK_TASK"));
  }

  @Test
  void notesDialog() {
    openView("tasks.xhtml");
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("actionMenuForm:taskNotesBtn")).shouldBe(visible).click();
    var dialog = $(By.id("actionMenuForm:taskNotesBtn_dlg"));
    dialog.shouldBe(visible);
    var iframe = dialog.find(By.tagName("iframe"));
    iframe.shouldBe(visible);
    Selenide.switchTo().frame(iframe);
    $(By.id("content")).shouldHave(text("this is test note"));
  }

  @Test
  void notesBtnDisabled() {
    startTestProcess("1750C5211D94569D/HomePageTestData.ivp");
    openView("tasks.xhtml");
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("actionMenuForm:taskNotesBtn")).shouldBe(visible).shouldNotBe(enabled);
  }

  @Test
  void taskNotFound() {
    openView("task.xhtml", Map.of("id", "NON-EXISTING-TASK"));
    assertThat(Selenide.webdriver().driver().getWebDriver().getPageSource()).contains("Not Found");
  }

  @Test
  void startTask() {
    openView("tasks.xhtml");

    $(".detail-btn").shouldBe(visible).click();
    $(".case-link").shouldHave(text("Created case of TestData"));

    $(By.id("taskState")).shouldBe(exactText("OPEN (SUSPENDED)"));
    $("#actionMenuForm\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"))
            .shouldBe(enabled).click();
    openView("tasks.xhtml");
    $(".detail-btn").shouldBe(visible).click();
    $("#taskName").shouldBe(exactText("Created task of TestData"));

    $(By.id("taskState")).shouldBe(exactText("DONE (DONE)"));
    $("#actionMenuForm\\:taskStartBtn").shouldHave(cssClass("ui-state-disabled"));
  }

  @Test
  void startParkedTask() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("tasks.xhtml");

    $(".detail-btn").shouldBe(visible).click();
    $(".case-link").shouldHave(text("Created case of TestData"));

    $(By.id("taskState")).shouldBe(exactText("OPEN (SUSPENDED)"));
    $("#actionMenuForm\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"));

    $("#actionMenuForm\\:taskActionsBtn").click();
    $("#actionMenuForm\\:taskParkBtn").should(visible).click();

    $(By.id("taskState")).shouldBe(exactText("OPEN (PARKED)"));
    $("#workingUser\\:userName").shouldBe(exactText($("#sessionUserName").getText()));
    $("#actionMenuForm\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"));
  }

  @Test
  void workflowEventsPermission() {
    openView("tasks.xhtml");
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.className("case-link")).shouldHave(text("Created case of TestData"));
    var taskId = $(By.id("taskId")).getText();
    $(By.id("workflowEvents:eventsTable")).shouldBe(visible);

    loginFromTable("testuser");
    openView("tasks.xhtml");
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("taskId")).shouldBe(exactText(taskId));
    $(By.id("workflowEvents:eventsTable")).shouldNotBe(visible);
    $(By.id("workflowEvents:noPermissionMessage")).shouldBe(visible).shouldHave(text("No permissions"));
  }

  @Test
  void taskCustomFields() {
    openView("tasks.xhtml");
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.className("case-link")).shouldHave(text("Created case of TestData"));

    Table fieldsTable = PrimeUi.table(By.id("customFields:customFieldsTable"));
    fieldsTable.valueAt(1, 0).contains("task test value");
  }

  @Test
  void checkDelayedTask() {
    startTestProcess("1750C5211D94569D/DelayedTestTask.ivp");
    openView("tasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("Created delayed task"));
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("taskName")).shouldBe(exactText("Created delayed task"));
    $(By.id("taskState")).shouldBe(exactText("DELAYED (DELAYED)"));
    $(By.id("delayDate:datetime")).shouldNotBe(exactText(""));
    $(By.id("actionMenuForm:taskStartBtn")).shouldHave(cssClass("ui-state-disabled"));
  }

  @Test
  void checkCustomResponsibleUser() {
    startTestProcess("1750C5211D94569D/customUser.ivp");
    openView("tasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("Created task of CustomUser"));
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("taskName")).shouldBe(exactText("Created task of CustomUser"));
    $(By.id("taskResponsible:userName")).shouldHave(text("CustomUserTest"));
  }

  @Test
  void delegateTask() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("tasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("Created task of TestData"));
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
  void clearDelayOnTask() {
    startTestProcess("1750C5211D94569D/DelayedTestTask.ivp");
    openView("tasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("Created delayed task"));
    $(By.className("detail-btn")).shouldBe(visible).click();

    $(By.id("taskState")).shouldBe(exactText("DELAYED (DELAYED)"));
    $(By.id("delayDate:datetime")).shouldNotBe(exactText(""));

    $(By.id("actionMenuForm:taskActionsBtn")).click();
    $(By.id("actionMenuForm:taskClearDelayBtn")).should(visible).click();

    $(By.id("taskState")).shouldBe(exactText("OPEN (SUSPENDED)"));
    $(By.id("delayDate:datetime")).shouldBe(exactText(""));
  }

  @Test
  void customFieldEmbedInFrame() {
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("embed in frame");
    $(By.id("startsForm:projectStarts:0:startName")).shouldBe(visible, text("Do not embed in Frame")).click();
    $(By.id("form:proceed")).shouldBe(visible).click();
    openView("tasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("notEmbedTask"));
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("actionMenuForm:taskStartBtn")).shouldNotHave(text("?taskUrl"));
  }
}
