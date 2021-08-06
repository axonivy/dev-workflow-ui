package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlEndsWith;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
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
import com.codeborne.selenide.Selenide;

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
    loginFromTable("testuser");
    assertCurrentUrlEndsWith("loginTable.xhtml");
    logout();
    $("#sessionUserName").shouldBe(text("Unknown User"));
  }

  @Test
  void testLoginTableSearch() throws Exception {
    Selenide.open(viewUrl("loginTable.xhtml"));
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
    Selenide.open(viewUrl("login.xhtml"));
    $("#loginForm\\:loginMessage").shouldNotBe(visible);
    WorkflowUiUtil.customLogin("sadgs", "sdgasgd");
    $("#loginForm\\:loginMessage").shouldBe(visible);
  }

  @Test
  void testRedirectIfNotLogggedIn() {
    loginFromTable("testuser");
    logout();
    Selenide.open(viewUrl("cases.xhtml"));
    assertCurrentUrlEndsWith("loginTable.xhtml");
    $("#loginMessage").shouldBe(visible).shouldHave(text("you need to login"));

    Selenide.open(viewUrl("tasks.xhtml"));
    assertCurrentUrlEndsWith("loginTable.xhtml");
    $("#loginMessage").shouldBe(visible).shouldHave(text("you need to login"));

    loginFromTable("testuser");
    Selenide.open(viewUrl("tasks.xhtml"));
    assertCurrentUrlEndsWith("tasks.xhtml");
  }

  @Test
  void testRedirectToOriginalUrl() {
    loginFromTable("testuser");
    logout();
    Selenide.open(viewUrl("cases.xhtml"));
    assertCurrentUrlEndsWith("loginTable.xhtml");
    $("#loginMessage").shouldBe(visible).shouldHave(text("you need to login"));
    $(byText("testuser")).click();
    assertCurrentUrlEndsWith("cases.xhtml");
  }

  @Test
  void testLoginTableRedirect() {
    loginFromTable("DifferentLogin");
    assertCurrentUrlEndsWith("login.xhtml");
    WorkflowUiUtil.customLogin("DifferentLogin", "DifferentPassword");
    $("#sessionUserName").shouldBe(text("DifferentLogin"));
  }

  @Test
  void testLoginTableHighlightCurrentUser() {
    loginFromTable("testuser");
    Selenide.open(viewUrl("loginTable.xhtml"));
    $("#loginTable\\:users_data > tr.ui-widget-content.ui-datatable-even.ui-datatable-selectable.ui-state-highlight")
            .shouldBe(visible);
    logout();
    Selenide.open(viewUrl("loginTable.xhtml"));
    $("#loginTable\\:users_data > tr.ui-widget-content.ui-datatable-even.ui-datatable-selectable.ui-state-highlight")
            .shouldNotBe(visible);
  }

}
