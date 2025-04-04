package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.login;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.readonly;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.codeborne.selenide.Selenide;

@IvyWebTest
class WebTestHomepageIT {

  @Test
  void homepageContainers() {
    login("DifferentLogin", "DifferentPassword");

    // start process to create test data
    openView("starts.xhtml");
    var starts = PrimeUi.table(By.id("startsForm:projectStarts"));
    starts.searchGlobal("test _ case _ map");
    starts.row(0).shouldHave(text("test _ case _ map")).find(".start-name").click();
    $(By.id("iFrameForm:frameTaskName")).shouldBe(text("Test Developer Workflow-UI Dialog 1"));
    $(By.id("iFrame")).shouldBe(visible);
    openView("home.xhtml");
    $(".startedProcessesCard").shouldBe(visible);

    // check if the data is in the containers
    PrimeUi.table(By.id("activeTasks")).contains("Test Developer Workflow-UI Dialog 1");
    PrimeUi.table(By.id("startedProcesses")).contains("test _ case _ map");
  }

  @Test
  void cardsIfLoggedIn() {
    loginDeveloper();
    $(".active-tasks-card").shouldBe(visible);
    $(".startedProcessesCard").shouldBe(visible);
  }

  @Test
  void homepageViewer() {
    loginDeveloper();
    openView("starts.xhtml");

    var starts = PrimeUi.table(By.id("startsForm:projectStarts"));
    starts.searchGlobal("test _ case _ map");
    starts.row(0).shouldHave(text("test _ case _ map")).find(".start-name").click();
    $(By.id("iFrame")).shouldBe(visible);
    openView("home.xhtml");
    $(".startedProcessesCard").shouldBe(visible);

    PrimeUi.table(By.id("startedProcesses")).contains("test _ case _ map");

    var caseMapeStartEntry = $("[data-rk='test _ case _ map']").shouldBe(visible);
    var menuLink = caseMapeStartEntry.$("button[id*='startedProcessMenuLink']").shouldBe(visible);
    menuLink.click();
    var menuLinkId = menuLink.attr("id");
    var processViewerId = menuLinkId.replace("startedProcessMenuLink", "openProcessViewer");
    var escapedProcessViewerId = processViewerId.replace(":", "\\:");
    $("#" + escapedProcessViewerId).shouldBe(visible).click();
    $(By.id("processViewer:processViewerDialog")).shouldBe(visible);

    $(By.id("viewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("viewerFrame");
    $(By.id("name-id")).should(readonly, value("test _ case _ map"));
    $(By.className("fa-apple")).shouldBe(visible);
  }

  @Test
  void homepageCaseRerun() {
    loginFromTable("testuser");
    startTestProcess("1750C5211D94569D/TestData.ivp");
    startTestProcess("1750C5211D94569D/customUser.ivp");
    openView("home.xhtml");

    var startedCasesTable = PrimeUi.table(By.id("startedCases"));
    startedCasesTable.valueAt(1, 1).contains("Created case of TestData");
    startedCasesTable.valueAt(0, 1).contains("Created case of CustomUser");
    startedCasesTable.valueAt(0, 0).contains("rerunCaseIcon");

    $(By.id("startedCases:0:caseActionsBtn")).shouldBe(visible).click();
    $(By.id("startedCases:0:rerunCaseProcessBtn")).shouldBe(visible).click();
    startedCasesTable = PrimeUi.table(By.id("startedCases"));
    startedCasesTable.valueAt(1, 1).contains("Created case of CustomUser");
    startedCasesTable.valueAt(0, 1).contains("Created case of CustomUser");
  }
}
