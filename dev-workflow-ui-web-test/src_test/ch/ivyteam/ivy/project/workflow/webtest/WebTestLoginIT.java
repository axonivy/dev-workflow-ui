package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlContains;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.open;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openViewNoAssertion;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.browserup.bup.filters.ResponseFilter;
import com.browserup.bup.util.HttpMessageContents;
import com.browserup.bup.util.HttpMessageInfo;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

import ch.ivyteam.ivy.project.workflow.webtest.util.Navigation;
import ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil;
import io.netty.handler.codec.http.HttpResponse;

@IvyWebTest
class WebTestLoginIT {
  static final String LOGIN = "login.xhtml";
  static final RecordLoginStatusCode STATUS = new RecordLoginStatusCode();
  private static boolean originalProxyEnabled;

  @BeforeAll
  static void beforeAll() {
    Selenide.closeWebDriver();
    originalProxyEnabled = Configuration.proxyEnabled;
    Configuration.proxyEnabled = true;
    openView("starts.xhtml");
    Selenide.webdriver().driver().getProxy().addResponseFilter(LOGIN, STATUS);
  }

  @AfterAll()
  static void afterAll() {
    Selenide.webdriver().driver().getProxy().removeRequestFilter(LOGIN);
    Configuration.proxyEnabled = originalProxyEnabled;
    Selenide.closeWebDriver();
  }

  @BeforeEach
  void init() {
    openView("starts.xhtml");
  }

  @Test
  void loginTable() {
    logout();
    loginFromTable("testuser");
    $("#sessionUserName").shouldBe(exactText("testuser"));
    Selenide.webdriver().shouldNotHave(urlContaining("/switch-user.xhtml"));
  }

  @Test
  void logoutUser() {
    logout();
    openViewNoAssertion("home.xhtml");
    loginFromTable("testuser");
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-nav-page"));
    assertCurrentUrlContains("home.xhtml");
    logout();
    $("#sessionUserName").shouldHave(text("Unknown User"));
  }

  @Test
  void switchUserSearch() throws Exception {
    openView("switch-user.xhtml");
    Table table = PrimeUi.table(By.id("loginTable:users"));
    table.contains("DeveloperTest");
    $(By.id("loginTable:globalFilter")).setValue("testuser");
    table.containsNot("DeveloperTest");
    table.contains("testuser");
  }

  @Test
  void customLogin() {
    WorkflowUiUtil.login("DeveloperTest", "DeveloperTest");
    assertThat(STATUS.code).isEqualTo(302);
    assertThat(STATUS.isAjax).isEqualTo(false);
  }

  @Test
  void customLoginFailMessage() {
    openView(LOGIN);
    $("#loginForm\\:loginMessage").shouldNotBe(visible);
    WorkflowUiUtil.tryLogin("sadgs", "sdgasgd");
    assertThat(STATUS.code).isEqualTo(401);
    assertThat(STATUS.isAjax).isEqualTo(false);
    $("#loginForm\\:loginMessage").shouldBe(visible);
  }

  @Test
  void redirectIfNotLogggedIn() {
    loginFromTable("testuser");
    logout();
    openViewNoAssertion("cases.xhtml");
    assertCurrentUrlContains("switch-user.xhtml?origin=cases");
    $("#loginMessage").shouldBe(visible).shouldHave(text("you need to login"));

    openViewNoAssertion("tasks.xhtml");
    assertCurrentUrlContains("switch-user.xhtml?origin=tasks");
    $("#loginMessage").shouldBe(visible).shouldHave(text("you need to login"));

    loginFromTable("testuser");
    openView("tasks.xhtml");
    assertCurrentUrlContains("tasks.xhtml");
  }

  @Test
  void redirectToOriginalPage() {
    loginFromTable("testuser");
    logout();
    openViewNoAssertion("cases.xhtml");
    assertCurrentUrlContains("switch-user.xhtml?origin=cases");
    $("#loginMessage").shouldBe(visible).shouldHave(text("you need to login"));
    $(byText("testuser")).click();
    assertCurrentUrlContains("cases.xhtml");
  }

  @Test
  void redirectToDifferentUrl_bothParams() {
    var originPage = "tasks.xhtml";
    var originalUrl = "cases.xhtml";

    openView(LOGIN, Map.of("originalUrl", originalUrl, "origin", originPage));
    $("#loginForm\\:loginMessage").shouldNotBe(visible);
    WorkflowUiUtil.fillLoginData("Developer", "Developer");
    assertCurrentUrlContains(originalUrl);

    // Different order
    openView(LOGIN, Map.of("origin", originPage, "originalUrl", originalUrl));
    $("#loginForm\\:loginMessage").shouldNotBe(visible);
    WorkflowUiUtil.fillLoginData("Developer", "Developer");
    assertCurrentUrlContains(originalUrl);
  }

  @Test
  void switchUserOnDetailPage() {
    loginFromTable("testuser");
    startTestProcess("1750C5211D94569D/TestData.ivp");
    Navigation.openTask("Created task of TestData");
    var taskId = $(By.id("taskId")).shouldBe(visible).text();
    assertCurrentUrlContains("task.xhtml?id=" + taskId);

    $(".user-profile").shouldBe(visible).click();
    $(By.id("loginTableBtn")).shouldBe(visible).click();
    assertCurrentUrlContains("switch-user.xhtml?origin=task%3Fid%3D" + taskId);
  }

  @Test
  void switchUserOnlyTestUsers() {
    open(viewUrl("switch-user.xhtml"));
    $(By.id("loginTable")).find(byText("testuser")).should(visible);
    $(By.id("loginTable")).find(byText("DifferentLogin")).shouldNot(exist);
  }

  @Test
  void switchUserHighlightCurrentUser() {
    loginFromTable("testuser");
    openView("switch-user.xhtml");
    $("#loginTable\\:users_data > .ui-state-highlight")
        .shouldBe(visible);
    logout();
    openView("switch-user.xhtml");
    $("#loginTable\\:users_data > .ui-state-highlight")
        .shouldNotBe(visible);
  }

  private static final class RecordLoginStatusCode implements ResponseFilter {
    int code;
    boolean isAjax;

    @Override
    public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
      if (messageInfo.getOriginalUrl().endsWith(LOGIN)) {
        code = response.status().code();
        var facesRequest = messageInfo.getOriginalRequest().headers().get("Faces-Request");
        isAjax = facesRequest != null && "partial/ajax".equals(facesRequest);
      }
    }
  }
}
