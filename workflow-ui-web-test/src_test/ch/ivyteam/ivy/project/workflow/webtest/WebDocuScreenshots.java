package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.executeJs;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

import ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil;

@IvyWebTest
public class WebDocuScreenshots
{
  private static final int SCREENSHOT_WIDTH = 1500;

  @BeforeEach
  void beforeEach()
  {
    Configuration.reportsFolder = "target/docu/screenshots/";
    Configuration.savePageSource = false;
  }

  @Test
  public void screenshotMainPages()
  {
    open(viewUrl("home.xhtml"));
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");

    open(viewUrl("home.xhtml"));
    takeScreenshot("workflow-ui-home", new Dimension(SCREENSHOT_WIDTH, 800));

    open(viewUrl("loginTable.xhtml"));
    takeScreenshot("workflow-ui-loginTable", new Dimension(SCREENSHOT_WIDTH, 800));

    open(viewUrl("login.xhtml"));
    takeScreenshot("workflow-ui-login", new Dimension(SCREENSHOT_WIDTH, 800));

    open(viewUrl("tasks.xhtml"));
    takeScreenshot("workflow-ui-tasks", new Dimension(SCREENSHOT_WIDTH, 800));

    open(viewUrl("allTasks.xhtml"));
    takeScreenshot("workflow-ui-allTasksAndCases", new Dimension(SCREENSHOT_WIDTH, 800));

    $(".si-information-circle").shouldBe(visible).click();
    $("#form\\:taskActionsBtn").click();
    takeScreenshot("workflow-ui-taskDetails", new Dimension(SCREENSHOT_WIDTH, 800));

    open(viewUrl("cases.xhtml"));
    takeScreenshot("workflow-ui-cases", new Dimension(SCREENSHOT_WIDTH, 800));

    $(".si-information-circle").shouldBe(visible).click();
    takeScreenshot("workflow-ui-caseDetails", new Dimension(SCREENSHOT_WIDTH, 800));

    open(viewUrl("starts.xhtml"));
    takeScreenshot("workflow-ui-starts", new Dimension(SCREENSHOT_WIDTH, 800));

    open(viewUrl("cleanup.xhtml"));
    takeScreenshot("workflow-ui-cleanup", new Dimension(SCREENSHOT_WIDTH, 800));

    open(viewUrl("signals.xhtml"));
    takeScreenshot("workflow-ui-signals", new Dimension(SCREENSHOT_WIDTH, 800));

    WorkflowUiUtil.startTestCaseMap("0cf1f054-a4ad-4b2b-bcf1-c9c34ec0a2ab.icm");
    open(viewUrl("cases.xhtml"));
    $(".si-information-circle").shouldBe(visible).click();
    takeScreenshot("workflow-ui-caseMap", new Dimension(SCREENSHOT_WIDTH, 800));

  }

  private void takeScreenshot(String fileName, Dimension size)
  {
    Dimension oldSize = WebDriverRunner.getWebDriver().manage().window().getSize();
    resizeBrowser(size);
    executeJs("scroll(0,0);");
    Selenide.screenshot(fileName);
    resizeBrowser(oldSize);
  }

  private void resizeBrowser(Dimension size)
  {
    WebDriverRunner.getWebDriver().manage().window().setSize(size);
  }
}
