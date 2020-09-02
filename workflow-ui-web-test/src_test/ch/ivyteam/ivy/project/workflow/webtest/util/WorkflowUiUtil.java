package ch.ivyteam.ivy.project.workflow.webtest.util;

import static com.axonivy.ivy.webtest.engine.EngineUrl.createStaticViewUrl;
import static org.assertj.core.api.Assertions.assertThat;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.codeborne.selenide.WebDriverRunner;

public class WorkflowUiUtil
{
  public static String pmvName()
  {
    return System.getProperty("test.engine.pmv", "workflow-ui");
  }

  public static String viewUrl(String page)
  {
    return createStaticViewUrl(pmvName() + "/" + page);
  }

  public static void assertCurrentUrlEndsWith(String endsWith)
  {
    String url = getCurrentUrl();
    if (url.contains(";jsessionid"))
    {
      url = url.substring(0, url.indexOf(";jsessionid"));
    }
    assertThat(url).endsWith(endsWith);
  }

  private static String getCurrentUrl()
  {
    return WebDriverRunner.getWebDriver().getCurrentUrl();
  }

  public static void executeJs(String js)
  {
    ((RemoteWebDriver) WebDriverRunner.getWebDriver()).executeScript(js);
  }
}
