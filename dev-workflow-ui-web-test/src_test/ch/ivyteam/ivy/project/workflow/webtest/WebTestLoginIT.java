package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlContains;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;

import ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil;

@IvyWebTest
public class WebTestLoginIT {

  @Test
  void testLoginTable() {
    loginFromTable("testuser");
    $("#sessionUserName").shouldBe(exactText("testuser"));
  }

  @Test
  void testLogout() {
    logout();
    openView("home.xhtml");
    loginFromTable("testuser");
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-menu"));
    assertCurrentUrlContains("home.xhtml");
    logout();
    $("#sessionUserName").shouldHave(text("Unknown User"));
  }

  @Test
  void testLoginTableSearch() throws Exception {
    openView("loginTable.xhtml");
    Table table = PrimeUi.table(By.id("loginTable:users"));
    table.contains("DeveloperTest");
    table.searchGlobal("testuser");
    table.containsNot("DeveloperTest");
    table.contains("testuser");
  }

  @Test
  void testCustomLogin() {
    WorkflowUiUtil.customLogin("DeveloperTest", "DeveloperTest");
  }

  @Test
  void testCustomLoginFailMessage() {
    openView("login.xhtml");
    $("#loginForm\\:loginMessage").shouldNotBe(visible);
    WorkflowUiUtil.customLogin("sadgs", "sdgasgd");
    $("#loginForm\\:loginMessage").shouldBe(visible);
  }

  @Test
  void testRedirectIfNotLogggedIn() {
    loginFromTable("testuser");
    logout();
    openView("cases.xhtml");
    assertCurrentUrlContains("loginTable.xhtml?originalUrl=cases.xhtml");
    $("#loginMessage").shouldBe(visible).shouldHave(text("you need to login"));

    openView("tasks.xhtml");
    assertCurrentUrlContains("loginTable.xhtml?originalUrl=tasks.xhtml");
    $("#loginMessage").shouldBe(visible).shouldHave(text("you need to login"));

    loginFromTable("testuser");
    openView("tasks.xhtml");
    assertCurrentUrlContains("tasks.xhtml");
  }

  @Test
  void testRedirectToOriginalUrl() {
    loginFromTable("testuser");
    logout();
    openView("cases.xhtml");
    assertCurrentUrlContains("loginTable.xhtml?originalUrl=cases.xhtml");
    $("#loginMessage").shouldBe(visible).shouldHave(text("you need to login"));
    $(byText("testuser")).click();
    assertCurrentUrlContains("cases.xhtml");
  }

  @Test
  void testLoginTableRedirect() {
    loginFromTable("DifferentLogin");
    assertCurrentUrlContains("login.xhtml?originalUrl=loginTable.xhtml");
    WorkflowUiUtil.customLogin("DifferentLogin", "DifferentPassword");
    $("#sessionUserName").shouldHave(text("DifferentLogin"));
  }

  @Test
  void testLoginTableHighlightCurrentUser() {
    loginFromTable("testuser");
    openView("loginTable.xhtml");
    $("#loginTable\\:users_data > .ui-state-highlight")
            .shouldBe(visible);
    logout();
    openView("loginTable.xhtml");
    $("#loginTable\\:users_data > .ui-state-highlight")
            .shouldNotBe(visible);
  }

}
