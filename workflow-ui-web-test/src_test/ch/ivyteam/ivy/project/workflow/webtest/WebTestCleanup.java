package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestCleanup
{
  @BeforeAll
  public static void makeAdminUser()
  {
    startTestProcess("1750C5211D94569D/designerEmbedded.ivp");
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @BeforeEach
  public void loginAdmin()
  {
    loginDeveloper();
  }

  @Test
  public void testCleanupButtonAdmin()
  {
    Selenide.open(viewUrl("cleanup.xhtml"));
    $(By.id("clanupForm:cleanupBtn")).shouldNotBe(disabled);
    loginFromTable("testuser");
    Selenide.open(viewUrl("cleanup.xhtml"));
    $(By.id("clanupForm:cleanupBtn")).shouldBe(disabled);
  }

  @Test
  public void testClean()
  {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    open(viewUrl("cases.xhtml"));
    Table table = PrimeUi.table(By.id("casesForm:cases"));
    table.firstRowContains("TestCase");

    Selenide.open(viewUrl("cleanup.xhtml"));
    $(By.id("clanupForm:cleanupBtn")).shouldBe(visible).click();
    $(By.id("casesForm:cases_data")).shouldNot(exist);
  }

  @Test
  public void testGrowl()
  {
    Selenide.open(viewUrl("cleanup.xhtml"));
    $(By.id("clanupForm:growl_container")).shouldNotBe(visible);
    $(By.id("clanupForm:cleanupBtn")).shouldNotBe(disabled).click();
    $(By.id("clanupForm:growl_container")).shouldBe(visible);
  }

  @Test
  public void testCleanupNoChecks()
  {
    Selenide.open(viewUrl("cleanup.xhtml"));
    $(By.id("clanupForm:casesAndTasksCheckbox")).shouldBe(enabled).click();
    $(By.id("clanupForm:businessDataCheckbox")).shouldBe(enabled).click();
    $(By.id("clanupForm:identityProviderTokenCheckbox")).shouldBe(enabled).click();
    $(By.id("clanupForm:cleanupBtn")).shouldNotBe(disabled).click();
    $(By.id("clanupForm:growl_container")).shouldNotBe(visible);
  }

  @Test
  public void testNoCleanupEngine()
  {
    Selenide.open(viewUrl("cleanup.xhtml"));
    $(By.id("clanupForm:cleanupBtn")).shouldBe(enabled);
    startTestProcess("1750C5211D94569D/maintenance.ivp");
    Selenide.open(viewUrl("cleanup.xhtml"));
    $(By.id("clanupForm:cleanupBtn")).shouldNotBe(enabled);
  }

}
