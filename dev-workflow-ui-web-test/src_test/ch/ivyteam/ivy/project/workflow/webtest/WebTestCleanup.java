package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

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
class WebTestCleanup {

  @BeforeAll
  static void prepare() {
    // check if the engine is running and starts are available
    checkProcessesExist();
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  private static void checkProcessesExist() {
    openView("starts.xhtml");
    var table = PrimeUi.table(By.id("startsForm:projectStarts"));
    table.searchGlobal("test");
    table.contains("startTestDialog");
    table.searchGlobal("case");
    table.contains("test _ case _ map");
  }

  @BeforeEach
  void loginAdmin() {
    loginDeveloper();
  }

  @Test
  @Order(1)
  void noCleanupIfNotDevMode() {
    openView("cleanup.xhtml");
    $(By.id("clanupForm:cleanupBtn")).shouldBe(disabled);
    startTestProcess("1783B19164F69B78/designerStandard.ivp");
    openView("cleanup.xhtml");
    $(By.id("clanupForm:cleanupBtn")).shouldBe(disabled);
  }

  @Test
  @Order(2)
  void cleanupUserPermissions() {
    startTestProcess("1783B19164F69B78/designerEmbedded.ivp");
    loginFromTable("testuser");
    $(By.id("menuform:sr_actions")).shouldBe(visible).click();
    $(By.id("menuform:sr_cleanup")).shouldNotBe(visible);
    open(viewUrl("cleanup.xhtml"));
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-nav-page"));

    loginDeveloper();
    $(By.id("menuform:sr_actions")).shouldBe(visible).click();
    $(By.id("menuform:sr_cleanup")).shouldBe(visible);
    openView("cleanup.xhtml");
    $(By.id("clanupForm:cleanupBtn")).shouldBe(enabled);
  }

  @Test
  @Order(3)
  void clean() {
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
    checkProcessesExist();

    openView("cleanup.xhtml");
    $(By.id("clanupForm:cleanupBtn")).shouldBe(visible).click();
    $(By.id("clanupForm:growl_container")).shouldBe(visible);

    openView("cases.xhtml");
    casesTable.row(0).shouldHave(text("No Cases found"));
    checkProcessesExist();
  }

}
