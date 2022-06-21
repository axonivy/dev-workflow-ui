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
    return System.getProperty("test.engine.pmv", "dev-workflow-ui");
  }

  public static void startTestProcess(String pathToIvp) {
    var url = EngineUrl.createProcessUrl("/dev-workflow-ui-test-data/" + pathToIvp);
    System.out.println(url);
    Selenide.open(url);
  }

  public static void startTestCaseMap(String path) {
    var url = EngineUrl.createCaseMapUrl("/dev-workflow-ui-test-data/" + path);
    System.out.println(url);
    Selenide.open(url);
  }

  public static String viewUrl(String page) {
    var securityContext = System.getProperty("test.integrated.workflow");
    if (StringUtils.isNotEmpty(securityContext)) {
      var engineUri = System.getProperty("test.engine.url");
      var url = engineUri + "/" + securityContext + "/faces/" + page;
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
    $("#loginForm\\:userName").clear();
    $("#loginForm\\:userName").sendKeys(username);
    $("#loginForm\\:password").clear();
    $("#loginForm\\:password").sendKeys(password);
    $("#loginForm\\:login").shouldBe(visible).click();
  }

  public static void logout() {
    if (!$("#sessionUserName").has(text("Unknown User"))) {
      $(".user-profile").click();
      $("#sessionLogoutBtn").shouldBe(visible).click();
    }
  }
}
