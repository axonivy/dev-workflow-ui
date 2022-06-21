package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestCaseDetailsIT {

  @BeforeAll
  static void setup() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
  }

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
    open(viewUrl("cases.xhtml"));
    $(byText("Created case of TestData")).shouldBe(visible).click();
    $(By.id("form:caseName")).shouldBe(text("Created case of TestData"));
  }

  @Test
  public void caseDetails() {
    $(By.id("form:creatorUser:userName")).shouldBe(exactText("DeveloperTest"));
    $(By.id("form:category")).shouldHave(exactText("TestData"));
    $(By.id("form:caseState")).shouldBe(exactText("RUNNING"));

    $(By.id("form:caseDestroyBtn")).should(visible).click();
    $(By.id("form:caseState")).shouldBe(exactText("DESTROYED"));
  }

  @Test
  public void taskList() throws Exception {
    Table tasksTable = PrimeUi.table(By.id("form:tasks"));
    tasksTable.valueAt(0, 0).contains("Test Task");
  }

  @Test
  public void checkTaskTableSystemTask() {
    startTestProcess("1750C5211D94569D/testIntermediateEventProcess.ivp");
    open(viewUrl("cases.xhtml"));
    $(".detail-btn").shouldBe(visible).click();

    Table tasksTable = PrimeUi.table(By.id("form:tasks"));
    tasksTable.containsNot("System");

    $(By.id("form:showSystemTasksSwitch")).shouldBe(visible).click();

    tasksTable.contains("System");
  }

  @Test
  public void caseList() throws Exception {
    $(".current-hierarchy-case").find("a").shouldHave(text("Created case of TestData"));
  }

  @Test
  public void customFields() throws Exception {
    Table fieldsTable = PrimeUi.table(By.id("form:customFields:customFieldsTable"));
    fieldsTable.valueAt(1, 0).contains("field 2");
  }

  @Test
  public void workflowEvents() {
    var caseId = $(By.id("form:caseId")).getText();
    $(By.id("form:workflowEvents:eventsTable")).shouldBe(visible);
    loginFromTable("testuser");
    open(viewUrl("cases.xhtml"));
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.id("form:caseId")).shouldBe(exactText(caseId));
    $(By.id("form:workflowEvents:eventsTable")).shouldNotBe(visible);
    $(By.id("form:workflowEvents:noPermissionMessage")).shouldBe(visible).shouldHave(text("No permissions"));
  }

  @Test
  public void destoryCase() {
    startTestProcess("1750C5211D94569D/startBoundarySignal.ivp");
    open(viewUrl("cases.xhtml"));
    $(By.id("casesForm:cases:0:caseName")).shouldBe(visible).click();
    $(By.id("form:caseState")).shouldBe(exactText("RUNNING"));
    $(By.id("form:caseDestroyBtn")).shouldBe(visible).shouldNotHave(cssClass("ui-state-disabled"));
    $(".current-hierarchy-case").findAll(".case-link").shouldBe(size(1));
    $(".current-hierarchy-case").findAll(".case-state-in-progress").shouldBe(size(1));

    $(By.id("form:caseDestroyBtn")).click();
    $(".current-hierarchy-case").findAll(".case-link").shouldBe(size(1));
    $(".current-hierarchy-case").findAll(".case-state-zombie-destroyed").shouldBe(size(1));
    $(By.id("form:caseDestroyBtn")).shouldHave(cssClass("ui-state-disabled"));
  }

  @Test
  public void processViewer() {
    $(By.id("processViewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("processViewerFrame");
    $(By.id("sprotty_1750C5211D94569D-f0")).is(visible);
  }

}
