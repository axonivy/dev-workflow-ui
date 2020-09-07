package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.executeJs;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

@IvyWebTest
public class WebTestScreenshotsIT
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
    takeScreenshot("workflow-ui-home", new Dimension(SCREENSHOT_WIDTH, 800));

    open(viewUrl("tasks.xhtml"));
    takeScreenshot("workflow-ui-tasks", new Dimension(SCREENSHOT_WIDTH, 800));

    open(viewUrl("starts.xhtml"));
    takeScreenshot("workflow-ui-starts", new Dimension(SCREENSHOT_WIDTH, 800));
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
