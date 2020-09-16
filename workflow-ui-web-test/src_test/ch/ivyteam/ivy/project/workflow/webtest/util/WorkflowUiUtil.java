package ch.ivyteam.ivy.project.workflow.webtest.util;

import static com.axonivy.ivy.webtest.engine.EngineUrl.createStaticViewUrl;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.workflowui.util.UserUtil;

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

  public static List<IUser> getUsers()
  {
    return UserUtil.getUsers();
  }

  public static void loginFromTable(String username)
  {
    Selenide.open(viewUrl("loginTable.xhtml"));
    if (getCurrentUrl().endsWith("loginTable.xhtml"))
    {
      List<WebElement> rows = WebDriverRunner.getWebDriver()
              .findElements(By.xpath(".//*[@id='loginTable:users']/div[2]/table/tbody/tr/td[1]"));
      for (WebElement row : rows)
      {
        if (row.getText().contains(username))
        {
          row.click();
        }
      }
    }
  }

  public static void logout()
  {
    $("#sessionUser").click();
    $("#sessionLogoutBtn").shouldBe(visible).click();
  }
}
