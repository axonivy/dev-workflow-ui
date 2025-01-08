package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

import ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil;

@IvyWebTest
class WebDocuScreenshots {
  private static final int SCREENSHOT_WIDTH = 1500;

  @BeforeEach
  void beforeEach() {
    Configuration.reportsFolder = "target/docu/screenshots/";
    Configuration.savePageSource = false;
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();
  }

  @Test
  void screenshotMainPages() {
    openView("home.xhtml");
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");

    openView("home.xhtml");
    takeScreenshot("workflow-ui-home", new Dimension(SCREENSHOT_WIDTH, 800));

    openView("loginTable.xhtml");
    takeScreenshot("workflow-ui-loginTable", new Dimension(SCREENSHOT_WIDTH, 800));

    openView("login.xhtml");
    takeScreenshot("workflow-ui-login", new Dimension(SCREENSHOT_WIDTH, 800));

    openView("tasks.xhtml");
    takeScreenshot("workflow-ui-tasks", new Dimension(SCREENSHOT_WIDTH, 800));

    $(By.id("tasksForm:tasks:0:taskName")).shouldBe(visible).click();
    $("#actionMenuForm\\:taskActionsBtn").click();
    takeScreenshot("workflow-ui-taskDetails", new Dimension(SCREENSHOT_WIDTH, 800));

    openView("cases.xhtml");
    takeScreenshot("workflow-ui-cases", new Dimension(SCREENSHOT_WIDTH, 800));

    $(By.id("casesForm:cases:0:caseName")).shouldBe(visible).click();
    takeScreenshot("workflow-ui-caseDetails", new Dimension(SCREENSHOT_WIDTH, 800));

    $(By.id("creatorUser:userNameLink")).shouldBe(visible).click();
    takeScreenshot("workflow-ui-userDetails", new Dimension(SCREENSHOT_WIDTH, 800));

    openView("starts.xhtml");
    takeScreenshot("workflow-ui-starts", new Dimension(SCREENSHOT_WIDTH, 800));

    openView("cleanup.xhtml");
    takeScreenshot("workflow-ui-cleanup", new Dimension(SCREENSHOT_WIDTH, 800));

    openView("signals.xhtml");
    $(By.id("signalForm:signalCodeInput_input")).sendKeys("Screenshot data signal");
    $(By.id("signalForm:signalBtn")).shouldBe(enabled).click();
    startTestProcess("1750C5211D94569D/startBoundarySignal.ivp");
    openView("signals.xhtml");
    takeScreenshot("workflow-ui-signals", new Dimension(SCREENSHOT_WIDTH, 800));
    $(By.id("boundarySignalsTable:0:sendSignalIcon")).shouldBe(visible).click();

    WorkflowUiUtil.startTestCaseMap();
    openView("cases.xhtml");
    $(By.id("casesForm:cases:0:caseName")).shouldBe(visible).click();
    takeScreenshot("workflow-ui-caseMap", new Dimension(SCREENSHOT_WIDTH, 800));

    startTestProcess("1750C5211D94569D/testIntermediateEventProcess.ivp");
    openView("intermediateEvents.xhtml");
    takeScreenshot("workflow-ui-intermediateEvents", new Dimension(SCREENSHOT_WIDTH, 800));

    $(byText("TestIntermediateEvent")).click();
    $(By.id("id")).shouldBe(visible);
    takeScreenshot("workflow-ui-intermediateElementDetails", new Dimension(SCREENSHOT_WIDTH, 800));

    openView("api-browser.xhtml");
    if ($(By.id("apiBrowser")).is(visible)) {
      Selenide.switchTo().frame("apiBrowser");
    }
    $(".computed-url").shouldBe(visible);
    takeScreenshot("workflow-ui-swagger-ui", new Dimension(SCREENSHOT_WIDTH, 800));
    Selenide.switchTo().defaultContent();

    openView("statistics.xhtml");
    $(By.id("taskStatisticsChart_canvas")).shouldBe(visible);
    $(By.id("topCaseCreatorsChart_canvas")).shouldBe(visible);
    takeScreenshot("workflow-ui-statistics", new Dimension(SCREENSHOT_WIDTH, 800));
  }

  private void takeScreenshot(String fileName, Dimension size) {
    Dimension oldSize = WebDriverRunner.getWebDriver().manage().window().getSize();
    resizeBrowser(size);
    Selenide.executeJavaScript("scroll(0,0);");
    Selenide.screenshot(fileName);
    resizeBrowser(oldSize);
  }

  private void resizeBrowser(Dimension size) {
    WebDriverRunner.getWebDriver().manage().window().setSize(size);
  }
}
