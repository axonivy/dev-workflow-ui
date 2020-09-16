package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlEndsWith;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestLoginIT
{

  @Test
  void testLoginTable()
  {
    loginFromTable("testuser");
    $("#sessionUserName").shouldBe(exactText("testuser"));
  }

  @Test
  void testLogout()
  {
    loginFromTable("testuser");
    assertCurrentUrlEndsWith("home.xhtml");
    logout();
    $("#sessionUserName").shouldBe(text("Unknown User"));
  }

  @Test
  void testLoginTableSearch() throws Exception
  {
    Selenide.open(viewUrl("loginTable.xhtml"));
    Table table = PrimeUi.table(By.id("loginTable:users"));
    table.contains("DeveloperTest");
    table.searchGlobal("testuser");
    table.containsNot("DeveloperTest");
    table.contains("testuser");
  }

  @Test
  void testCustomLogin()
  {
    Selenide.open(viewUrl("login.xhtml"));
    $("#loginForm\\:username").sendKeys("DeveloperTest");
    $("#loginForm\\:password").sendKeys("DeveloperTest");
    $("#loginForm\\:login").shouldBe(visible).click();
  }

}
