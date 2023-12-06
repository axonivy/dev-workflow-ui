package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlContains;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

import ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil;

@IvyWebTest
public class WebTestLoginIT {

  @BeforeEach
  void init() {
    openView("starts.xhtml");
  }

  @Test
  void loginTable() {
    logout();
    loginFromTable("testuser");
    $("#sessionUserName").shouldBe(exactText("testuser"));
    Selenide.webdriver().shouldNotHave(urlContaining("/loginTable.xhtml"));
  }

  @Test
  void logoutUser() {
    logout();
    openView("home.xhtml");
    loginFromTable("testuser");
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-menuitem"));
    assertCurrentUrlContains("home.xhtml");
    logout();
    $("#sessionUserName").shouldHave(text("Unknown User"));
  }

  @Test
  void loginTableSearch() throws Exception {
    openView("loginTable.xhtml");
    Table table = PrimeUi.table(By.id("loginTable:users"));
    table.contains("DeveloperTest");
    table.searchGlobal("testuser");
    table.containsNot("DeveloperTest");
    table.contains("testuser");
  }

  @Test
  void customLogin() {
    WorkflowUiUtil.customLogin("DeveloperTest", "DeveloperTest");
    Selenide.webdriver().shouldNotHave(urlContaining("/login.xhtml"));
  }

  @Test
  void customLoginFailMessage() {
    openView("login.xhtml");
    $("#loginForm\\:loginMessage").shouldNotBe(visible);
    WorkflowUiUtil.customLogin("sadgs", "sdgasgd");
    $("#loginForm\\:loginMessage").shouldBe(visible);
  }

  @Test
  void redirectIfNotLogggedIn() {
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
  void redirectToOriginalUrl() {
    loginFromTable("testuser");
    logout();
    openView("cases.xhtml");
    assertCurrentUrlContains("loginTable.xhtml?originalUrl=cases.xhtml");
    $("#loginMessage").shouldBe(visible).shouldHave(text("you need to login"));
    $(byText("testuser")).click();
    assertCurrentUrlContains("cases.xhtml");
  }

  @Test
  void switchUserOnDetailPage() {
    loginFromTable("testuser");
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("allTasks.xhtml");
    $(".detail-btn").shouldBe(visible).click();
    var taskId = $(By.id("taskId")).shouldBe(visible).text();
    assertCurrentUrlContains("task.xhtml?id=" + taskId);

    $(".user-profile").shouldBe(visible).click();
    $(By.id("loginTableBtn")).shouldBe(visible).click();
    assertCurrentUrlContains("loginTable.xhtml?originalUrl=task.xhtml%3Fid%3D" + taskId);
  }

  @Test
  void loginTableRedirect() {
    loginFromTable("DifferentLogin");
    assertCurrentUrlContains("login.xhtml?originalUrl=loginTable.xhtml");
    WorkflowUiUtil.customLogin("DifferentLogin", "DifferentPassword");
    $("#sessionUserName").shouldHave(text("DifferentLogin"));
  }

  @Test
  void loginTableHighlightCurrentUser() {
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
