package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlContains;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openViewNoAssertion;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.checked;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

import ch.ivyteam.ivy.project.workflow.webtest.util.Navigation;

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
    Navigation.openTask("Created task of HomePageTestData");
    String taskId = $(By.id("taskId")).text();
    $(By.id("actionMenuForm:taskStartBtn")).shouldBe(enabled).click();

    loginFromTable("testuser");
    Navigation.openTasks();
    var table = PrimeUi.table(By.id("tasksForm:tasks"));
    if ("Created task of HomePageTestData".equals(table.row(0).text())) {
      $(By.id("tasksForm:tasks:0:taskName")).shouldBe(visible).click();
      $(By.id("taskId")).shouldNotBe(text(taskId));
    }
  }

  @Test
  void tasksTable() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    Navigation.openTasks();
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("Created task of TestData"));
    table.valueAt(0, 1).contains("pause");
  }

  @Test
  void systemTasks() {
    startTestProcess("1750C5211D94569D/testIntermediateEventProcess.ivp");
    Navigation.openTasks();
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.contains("System user");

    $(By.id("tasksForm:tasks:showAllTasksSwitch_input")).shouldBe(checked);
    $(By.id("tasksForm:tasks:showAllTasksSwitch")).click();
    $(By.id("tasksForm:tasks:showAllTasksSwitch_input")).shouldNotBe(checked);
    table.containsNot("System user");
  }

  @Test
  void checkTaskDetails() {
    Navigation.openTask("Created task of TestData");
    $(".case-link").shouldHave(text("Created case of TestData"));
    $(By.id("taskResponsibles:0:taskResponsible:userName")).shouldBe(exactText("Everybody"));
    $(By.id("taskState:stateBadge")).hover();
    $(By.id("taskState:tooltip")).$(".ui-tooltip-text").shouldHave(text("SUSPENDED"));
    $(By.id("category")).shouldHave(exactText("TaskWithACategory"));
    $(By.id("pmv")).shouldBe(exactText("dev-workflow-ui-test-data$1"));
    $(By.className("si-hierarchy-6")).shouldBe(visible);

    $(By.id("workflowEvents:eventsTable:0:eventType")).shouldBe(exactText("EVENT_CREATE_TASK_BY_JOINED_TASKS"));
    $(By.id("actionMenuForm:taskActionsBtn")).click();
    $(By.id("actionMenuForm:taskParkBtn")).shouldBe(visible).click();
    $(By.id("actionMenuForm:actionsMenu")).shouldNotBe(visible);

    $(By.tagName("body")).hover();
    $(By.id("taskState:stateBadge")).hover();
    $(By.id("taskState:tooltip")).$(".ui-tooltip-text").shouldHave(text("PARKED"));
    $(By.id("workingUser:userNameLink")).shouldBe(exactText($("#sessionUserName").getText()));
    $(By.id("workflowEvents:eventsTable:0:eventType")).shouldBe(exactText("EVENT_PARK_TASK"));
  }

  @Test
  void notesDialog() {
    Navigation.openTask("Created task of TestData");
    $(By.id("actionMenuForm:taskNotesBtn")).shouldBe(visible).click();
    var dialog = $(By.id("actionMenuForm:taskNotesBtn_dlg"));
    dialog.shouldBe(visible);
    var iframe = dialog.find(By.tagName("iframe"));
    iframe.shouldBe(visible);
    Selenide.switchTo().frame(iframe);
    $(By.id("content")).shouldHave(text("this is a test note"));
  }

  @Test
  void notesBtnDisabled() {
    startTestProcess("1750C5211D94569D/HomePageTestData.ivp");
    Navigation.openTask("Created task of HomePageTestData");
    $(By.id("actionMenuForm:taskNotesBtn")).shouldBe(visible).shouldNotBe(enabled);
  }

  @Test
  void taskNotFound() {
    openView("task.xhtml", Map.of("id", "NON-EXISTING-TASK"));
    $("body").shouldHave(text("Not Found"));
  }

  @Test
  void startTask() {
    Navigation.openTask("Created task of TestData");
    $(By.id("taskState:stateBadge")).hover();
    $(By.id("taskState:tooltip")).$(".ui-tooltip-text").shouldHave(text("SUSPENDED"));
    $(By.id("actionMenuForm:taskStartBtn")).shouldNotHave(cssClass("ui-state-disabled"))
        .shouldBe(enabled).click();
    assertCurrentUrlContains("/task.xhtml?id=");

    $(By.tagName("body")).hover();
    $(By.id("taskState:stateBadge")).hover();
    $(By.id("taskState:tooltip")).$(".ui-tooltip-text").shouldHave(text("DONE"));
    $(By.id("actionMenuForm:taskStartBtn")).shouldHave(cssClass("ui-state-disabled"));
  }

  @Test
  void startParkedTask() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    Navigation.openTask("Created task of TestData");
    $(By.id("taskState:stateBadge")).hover();
    $(By.id("taskState:tooltip")).$(".ui-tooltip-text").shouldHave(text("SUSPENDED"));
    $("#actionMenuForm\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"));

    $("#actionMenuForm\\:taskActionsBtn").click();
    $("#actionMenuForm\\:taskParkBtn").should(visible).click();

    $(By.tagName("body")).hover();
    $(By.id("taskState:stateBadge")).hover();
    $(By.id("taskState:tooltip")).$(".ui-tooltip-text").shouldHave(text("PARKED"));
    $("#workingUser\\:userNameLink").shouldBe(exactText($("#sessionUserName").getText()));
    $("#actionMenuForm\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"));
  }

  @Test
  void workflowEventsPermission() {
    Navigation.openTask("Created task of TestData");
    var taskId = $(By.id("taskId")).getText();
    $(By.id("workflowEvents:eventsTable")).shouldBe(visible);

    loginFromTable("testuser");
    openView("task.xhtml", Map.of("id", taskId));
    $(By.id("workflowEvents:eventsTable")).shouldNotBe(visible);
    $(By.id("workflowEvents:noPermissionMessage")).shouldBe(visible).shouldHave(text("No permissions"));
  }

  @Test
  void taskCustomFields() {
    Navigation.openTask("Created task of TestData");
    Table fieldsTable = PrimeUi.table(By.id("customFields:customFieldsTable"));
    fieldsTable.valueAt(1, 0).contains("task test value");
  }

  @Test
  void checkDelayedTask() {
    startTestProcess("1750C5211D94569D/DelayedTestTask.ivp");
    Navigation.openTask("Created delayed task");
    $(By.id("taskState:stateBadge")).hover();
    $(By.id("taskState:tooltip")).$(".ui-tooltip-text").shouldHave(text("DELAYED"));
    $(By.id("delayDate:datetime")).shouldNotBe(exactText(""));
    $(By.id("actionMenuForm:taskStartBtn")).shouldHave(cssClass("ui-state-disabled"));
  }

  @Test
  void checkCustomResponsibleUser() {
    startTestProcess("1750C5211D94569D/customUser.ivp");
    Navigation.openTask("Created task of CustomUser");
    $(By.id("taskResponsibles:0:taskResponsible:userName")).shouldHave(text("CustomUserTest"));
  }

  @Test
  void delegateTask() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    Navigation.openTask("Created task of TestData");
    $(By.id("taskResponsibles:0:taskResponsible:userName")).shouldBe(exactText("Everybody"));

    $(By.id("actionMenuForm:taskActionsBtn")).click();
    $(By.id("actionMenuForm:taskDelegateBtn")).should(visible).click();
    $(By.id("delegateTaskDialog")).shouldBe(visible);
    PrimeUi.selectOne(By.id("delegateTaskForm:selectUserMenu")).selectItemByLabel("testuser");

    $(By.id("delegateTaskForm:delegateProceedButton")).click();
    $(By.id("taskResponsibles:0:taskResponsible:userNameLink")).shouldHave(text("testuser"));
  }

  @Test
  void clearDelayOnTask() {
    startTestProcess("1750C5211D94569D/DelayedTestTask.ivp");
    Navigation.openTask("Created delayed task");
    $(By.id("taskState:stateBadge")).hover();
    $(By.id("taskState:tooltip")).$(".ui-tooltip-text").shouldHave(text("DELAYED"));
    $(By.id("delayDate:datetime")).shouldNotBe(exactText(""));

    $(By.id("actionMenuForm:taskActionsBtn")).click();
    $(By.id("actionMenuForm:taskClearDelayBtn")).should(visible).click();

    $(By.id("taskState:stateBadge")).hover();
    $(By.id("taskState:tooltip")).$(".ui-tooltip-text").shouldHave(text("SUSPENDED"));
    $(By.id("delayDate:datetime")).shouldBe(exactText(""));
  }

  @Test
  void customFieldEmbedInFrame() {
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("embed in frame").pressEnter();
    $(By.id("startsForm:projectStarts:0:startName")).shouldBe(visible, text("Do not embed in Frame")).click();
    $(By.id("form:proceed")).shouldBe(visible).click();

    Navigation.openTask("notEmbedTask");
    $(By.id("actionMenuForm:taskStartBtn")).shouldNotHave(text("?taskUrl"));

    var taskStartLink = $(By.id("actionMenuForm:taskStartBtn")).find(By.tagName("a")).getAttribute("href");
    Selenide.open(taskStartLink);
    $(By.id("iFrameForm")).shouldNotBe(visible);

    taskStartLink = taskStartLink + "&embedInFrame";
    Selenide.open(taskStartLink);
    $(By.id("iFrameForm")).shouldNotBe(visible);
  }

  @Test
  void iframeBreakout() {
    Map<String, String> params = new HashMap<>();
    var restUrl = EngineUrl.createRestUrl("iframe/noiframe").replace(EngineUrl.base(), "");
    params.put("origin", "home");
    params.put("taskUrl", restUrl);
    openViewNoAssertion("frame.xhtml", params);
    assertCurrentUrlContains(restUrl);
  }

  @Test
  void searchById() {
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    Navigation.openTask("Created task of TestData");
    var taskId = $(By.id("taskId")).getText();

    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("tasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).should(exist);
    table.row(1).should(exist);

    $(By.id("tasksForm:tasks:globalFilter")).setValue(taskId).pressEnter();

    $(By.id("tasksForm:tasks_data")).findAll(By.tagName("tr")).shouldBe(size(1));

    table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).should(exist);
    table.row(1).shouldNot(exist);
  }
}
