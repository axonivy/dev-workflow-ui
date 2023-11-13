package ch.ivyteam.ivy.project.workflow.webtest.util;

import static com.axonivy.ivy.webtest.engine.EngineUrl.createCaseMapUrl;
import static com.axonivy.ivy.webtest.engine.EngineUrl.createProcessUrl;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.engine.EngineUrl;

import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.workflowui.util.UserUtil;

public class WorkflowUiUtil {
  public static String pmvName() {
    return System.getProperty("test.engine.pmv", "dev-workflow-ui");
  }

  public static void startTestProcess(String pathToIvp) {
    open(createProcessUrl("/dev-workflow-ui-test-data/" + pathToIvp));
    assertCurrentUrlContains("end.xhtml");
  }

  public static void startTestCaseMap() {
    open(createCaseMapUrl("/dev-workflow-ui-test-data/0cf1f054-a4ad-4b2b-bcf1-c9c34ec0a2ab.icm"));
    $(By.id("form:proceed")).shouldBe(visible).click();
    assertCurrentUrlContains("end.xhtml");
  }

  public static void openView(String page) {
    openView(page, Map.of());
  }

  public static void openView(String page, Map<String, String> queryParams) {
    open(viewUrl(page, queryParams));
    assertCurrentUrlContains(page);
  }

  public static String viewUrl(String page) {
    return viewUrl(page, Map.of());
  }

  public static String viewUrl(String page, Map<String, String> queryParams) {
    var securityContext = System.getProperty("test.integrated.workflow");
    if (StringUtils.isNotEmpty(securityContext)) {
      var engineUri = System.getProperty("test.engine.url");
      var url = engineUri + "/" + securityContext + "/faces/" + page;
      if (!queryParams.isEmpty()) {
        url += "?";
      }
      for (var param : queryParams.entrySet()) {
        url += param.getKey() + "=" + param.getValue() + "&";
      }
      System.out.println("Engine URL integrated:" + url);
      return url;
    }
    var builder = EngineUrl.create().staticView(pmvName() + "/" + page);
    for (var param : queryParams.entrySet()) {
      builder.queryParam(param.getKey(), param.getValue());
    }
    var url = builder.toUrl();
    System.out.println("Engine URL:" + url);
    return url;
  }

  public static void assertCurrentUrlContains(String contains) {
    webdriver().shouldHave(urlContaining(contains));
  }

  public static List<IUser> getUsers() {
    return UserUtil.getUsers();
  }

  public static void loginFromTable(String username) {
    open(viewUrl("loginTable.xhtml"));
    $(By.id("loginTable")).find(byText(username)).should(visible).click();
    $(By.id("loginTable")).shouldNot(exist);
  }

  public static void loginDeveloper() {
    customLogin("DeveloperTest", "DeveloperTest");
  }

  public static void customLogin(String username, String password) {
    open(viewUrl("login.xhtml"));
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
