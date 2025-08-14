package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

import ch.ivyteam.ivy.project.workflow.webtest.util.Navigation;

@IvyWebTest
class WebTestCaseDetailsIT {

  @BeforeAll
  static void setup() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    Navigation.openCase("Created case of TestData");
  }

  @Test
  void caseDetails() {
    $(By.id("creatorUser:userNameLink")).shouldBe(exactText("DeveloperTest"));
    $(By.id("category")).shouldHave(exactText("TestData"));

    $(By.id("caseState:stateBadge")).hover();
    $(By.id("caseState:tooltip")).$(".ui-tooltip-text").shouldHave(text("RUNNING"));

    $(By.id("caseDestroyBtn")).should(visible).click();
    $(By.tagName("body")).hover();
    $(By.id("caseState:stateBadge")).hover();
    $(By.id("caseState:tooltip")).$(".ui-tooltip-text").shouldHave(text("DESTROYED"));
  }

  @Test
  void caseNotFound() {
    openView("case.xhtml", Map.of("id", "NON-EXISTING-CASE"));
    $("body").shouldHave(text("Not Found"));
  }

  @Test
  void taskList() {
    Table tasksTable = PrimeUi.table(By.id("tasksForm:tasks"));
    tasksTable.valueAtShouldBe(0, 2, text("First task of TestData"));

    $(By.id("tasksForm:tasks:0:taskActionsBtn")).shouldBe(visible).click();
    $(By.id("tasksForm:tasks:0:openTaskDetailsBtn")).shouldBe(visible).click();

    $(By.id("taskName")).shouldBe(text("First task of TestData"));
  }

  @Test
  void checkTaskTableSystemTask() {
    startTestProcess("1750C5211D94569D/testIntermediateEventProcess.ivp");
    Navigation.openCase("testintermediateEventProcess case");

    Table tasksTable = PrimeUi.table(By.id("tasksForm:tasks"));
    tasksTable.containsNot("System");

    $(By.id("tasksForm:showSystemTasksSwitch")).shouldBe(visible).click();

    tasksTable.contains("System");
  }

  @Test
  void caseList() {
    $(".current-hierarchy-case").find("a").shouldHave(text("Created case of TestData"));
  }

  @Test
  void customFields() {
    Table fieldsTable = PrimeUi.table(By.id("customFields:customFieldsTable"));
    fieldsTable.valueAtShouldBe(0, 0, text("field 2"));
  }

  @Test
  void workflowEvents() {
    var caseId = $(By.id("caseId")).getText();
    $(By.id("workflowEvents:eventsTable")).shouldBe(visible);
    loginFromTable("testuser");
    openView("case.xhtml", Map.of("id", caseId));
    $(By.id("caseId")).shouldBe(visible).shouldHave(exactText(caseId));
    $(By.id("workflowEvents:eventsTable")).shouldNotBe(visible);
    $(By.id("workflowEvents:noPermissionMessage")).shouldBe(visible).shouldHave(text("No permissions"));
  }

  @Test
  void notesDialog() {
    $(By.id("caseNotesBtn")).shouldBe(visible).click();
    var dialog = $(By.id("caseNotesBtn_dlg"));
    dialog.shouldBe(visible);
    var iframe = dialog.find(By.tagName("iframe"));
    iframe.shouldBe(visible);
    Selenide.switchTo().frame(iframe);
    $(By.id("content")).shouldHave(text("this is a test note"));
  }

  @Test
  void destoryCase() {
    startTestProcess("1750C5211D94569D/startBoundarySignal.ivp");
    Navigation.openCase("case created in UserTask during startBoundarySignal process");
    $(By.id("caseState:stateBadge")).hover();
    $(By.id("caseState:tooltip")).$(".ui-tooltip-text").shouldHave(text("RUNNING"));
    $(By.id("caseDestroyBtn")).shouldBe(visible).shouldNotHave(cssClass("ui-state-disabled"));
    $(".current-hierarchy-case").findAll(By.id("businessCase:caseLink")).shouldBe(size(1)).get(0).hover();
    $(By.id("businessCase:caseState:stateName")).shouldHave(text("open"));

    $(By.id("caseDestroyBtn")).click();
    $(By.tagName("body")).hover();
    $(".current-hierarchy-case").findAll(By.id("businessCase:caseLink")).shouldBe(size(1)).get(0).hover();
    $(By.id("businessCase:caseState:stateName")).shouldHave(text("destroyed"));
    $(By.id("caseDestroyBtn")).shouldHave(cssClass("ui-state-disabled"));
  }

  @Test
  void processViewer() {
    $(By.id("processViewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("processViewerFrame");
    $(By.id("sprotty_1750C5211D94569D-f0")).is(visible);
  }
}
