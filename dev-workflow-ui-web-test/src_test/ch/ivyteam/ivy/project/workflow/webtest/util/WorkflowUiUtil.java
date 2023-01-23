package ch.ivyteam.ivy.project.workflow.webtest.util;

import static com.axonivy.ivy.webtest.engine.EngineUrl.createStaticViewUrl;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverConditions;

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
    assertCurrentUrlContains("starts.xhtml");
  }

  public static void startTestCaseMap(String path) {
    var url = EngineUrl.createCaseMapUrl("/dev-workflow-ui-test-data/" + path);
    System.out.println(url);
    Selenide.open(url);
    assertCurrentUrlContains("starts.xhtml");
  }

  public static void openView(String page) {
    Selenide.open(viewUrl(page));
    assertCurrentUrlContains(page);
  }

  private static String viewUrl(String page) {
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

  public static void assertCurrentUrlContains(String contains) {
    WebDriverConditions.currentFrameUrlContaining(contains);
  }

  public static List<IUser> getUsers() {
    return UserUtil.getUsers();
  }

  public static void loginFromTable(String username) {
    Selenide.open(viewUrl("loginTable.xhtml"));
    $(byText(username)).should(visible).click();
    Selenide.sleep(50);
    $(By.id("sessionUserName")).has(text(username));
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
    assertCurrentUrlContains("home.xhtml");
  }

  public static void logout() {
    if (!$("#sessionUserName").has(text("Unknown User"))) {
      $(".user-profile").click();
      $("#sessionLogoutBtn").shouldBe(visible).click();
    }
  }
}
