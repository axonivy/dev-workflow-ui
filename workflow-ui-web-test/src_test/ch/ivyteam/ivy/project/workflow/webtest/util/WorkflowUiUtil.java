package ch.ivyteam.ivy.project.workflow.webtest.util;

import static com.axonivy.ivy.webtest.engine.EngineUrl.createStaticViewUrl;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.workflowui.util.UserUtil;

public class WorkflowUiUtil {
  public static String pmvName() {
    return System.getProperty("test.engine.pmv", "workflow-ui");
  }

  public static void startTestProcess(String pathToIvp) {
    Selenide.open(EngineUrl.createProcessUrl("/workflow-ui-test-data/" + pathToIvp));
  }

  public static void startTestCaseMap(String path) {
    Selenide.open(EngineUrl.createCaseMapUrl("/workflow-ui-test-data/" + path));
  }

  public static String viewUrl(String page) {
    if ("true".equalsIgnoreCase(System.getProperty("test.integrated.workflow"))) {
      String url = EngineUrl.create().path("/faces/" + page).toUrl();
      System.out.println("Engine URL integrated:" + url);
      return url;
    }
    String url = createStaticViewUrl(pmvName() + "/" + page);
    System.out.println("Engine URL:" + url);
    return url;
  }

  public static void assertCurrentUrlEndsWith(String endsWith) {
    String url = getCurrentUrl();
    if (url.contains(";jsessionid")) {
      url = url.substring(0, url.indexOf(";jsessionid"));
    }
    assertThat(url).endsWith(endsWith);
  }

  private static String getCurrentUrl() {
    return WebDriverRunner.getWebDriver().getCurrentUrl();
  }

  public static void executeJs(String js) {
    ((RemoteWebDriver) WebDriverRunner.getWebDriver()).executeScript(js);
  }

  public static List<IUser> getUsers() {
    return UserUtil.getUsers();
  }

  public static void loginFromTable(String username) {
    Selenide.open(viewUrl("loginTable.xhtml"));
    if (StringUtils.substringAfterLast(getCurrentUrl(), "/").startsWith("loginTable.xhtml")) {
      $(byText(username)).click();
    }
  }

  public static void loginDeveloper() {
    customLogin("DeveloperTest", "DeveloperTest");
  }

  public static void customLogin(String username, String password) {
    Selenide.open(viewUrl("login.xhtml"));
    $("#loginForm\\:username").clear();
    $("#loginForm\\:username").sendKeys(username);
    $("#loginForm\\:password").clear();
    $("#loginForm\\:password").sendKeys(password);
    $("#loginForm\\:login").shouldBe(visible).click();
  }

  public static void logout() {
    if (!$("#sessionUserName").has(text("Unknown User"))) {
      $("#sessionUser").click();
      $("#sessionLogoutBtn").shouldBe(visible).click();
    }
  }
}
