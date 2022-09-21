package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;

@IvyWebTest
@TestMethodOrder(OrderAnnotation.class)
public class WebTestCleanup {

  @BeforeAll
  public static void prepare() {
    // check if the engine is running and starts are available
    openView("starts.xhtml");
    $(By.id("startsForm:globalFilter")).sendKeys("workflow-ui-test-data");
    $("#startsForm\\:projectStarts\\:0\\:caseMapsTitle").shouldBe(visible);
    $("#startsForm\\:projectStarts\\:0\\:processesTitle").shouldBe(visible);
    $("#startsForm\\:projectStarts\\:0\\:webServicesTitle").shouldBe(visible);
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @BeforeEach
  public void loginAdmin() {
    loginDeveloper();
  }

  @Test
  @Order(1)
  public void testNoCleanupEngine() {
    openView("cleanup.xhtml");
    $(By.id("clanupForm:cleanupBtn")).shouldBe(enabled);
    startTestProcess("1783B19164F69B78/designerStandard.ivp");
    openView("cleanup.xhtml");
    $(By.id("clanupForm:cleanupBtn")).shouldNotBe(enabled);
  }

  @Test
  @Order(2)
  public void testCleanupUsers() {
    startTestProcess("1783B19164F69B78/designerEmbedded.ivp");

    loginFromTable("testuser");
    openView("cleanup.xhtml");
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-menuitem"));

    loginDeveloper();
    openView("cleanup.xhtml");
    $(By.id("clanupForm:cleanupBtn")).shouldNotBe(disabled);
  }

  @Test
  @Order(3)
  public void testClean() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("cases.xhtml");
    Table casesTable = PrimeUi.table(By.id("casesForm:cases"));
    casesTable.row(0).shouldHave(text("Created case of TestData"));

    openView("cleanup.xhtml");
    $(By.id("clanupForm:growl_container")).shouldNotBe(visible);

    $(By.id("clanupForm:casesAndTasksCheckbox")).shouldBe(enabled).click();
    $(By.id("clanupForm:businessDataCheckbox")).shouldBe(enabled).click();
    $(By.id("clanupForm:identityProviderTokenCheckbox")).shouldBe(enabled).click();
    $(By.id("clanupForm:dataCaches")).shouldBe(enabled).click();
    $(By.id("clanupForm:cleanupBtn")).shouldNotBe(disabled).click();
    $(By.id("clanupForm:growl_container")).shouldNotBe(visible);

    openView("cases.xhtml");
    casesTable.row(0).shouldHave(text("Created case of TestData"));
    openView("starts.xhtml");
    $(By.id("startsForm:globalFilter")).sendKeys("workflow-ui-test-data");
    $("#startsForm\\:projectStarts\\:0\\:caseMapsTitle").shouldBe(visible);
    $("#startsForm\\:projectStarts\\:0\\:processesTitle").shouldBe(visible);
    $("#startsForm\\:projectStarts\\:0\\:webServicesTitle").shouldBe(visible);

    openView("cleanup.xhtml");
    $(By.id("clanupForm:cleanupBtn")).shouldBe(visible).click();
    $(By.id("clanupForm:growl_container")).shouldBe(visible);

    openView("cases.xhtml");
    casesTable.row(0).shouldHave(text("No Cases found"));
    openView("starts.xhtml");
    $(By.id("startsForm:globalFilter")).sendKeys("workflow-ui-test-data");
    $("#startsForm\\:projectStarts\\:0\\:caseMapsTitle").shouldBe(visible);
    $("#startsForm\\:projectStarts\\:0\\:processesTitle").shouldBe(visible);
    $("#startsForm\\:projectStarts\\:0\\:webServicesTitle").shouldBe(visible);
  }

}
