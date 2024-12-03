package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;

@IvyWebTest
class WebTestCasesIT {

  @BeforeAll
  static void setup() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @Test
  void allCasesOnlyAdmin() {
    loginFromTable("testuser");
    openView("cases.xhtml");
    $(By.id("casesForm:cases:showAllCasesSwitch")).shouldNotBe(visible);
    loginDeveloper();
    openView("cases.xhtml");
    $(By.id("casesForm:cases:showAllCasesSwitch")).shouldBe(visible);
    $(By.id("casesForm:cases:showAllCasesSwitch_input")).shouldBe(enabled);
  }

  @Test
  void casesTable() throws Exception {
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("cases.xhtml");
    Table table = PrimeUi.table(By.id("casesForm:cases"));
    table.row(0).shouldHave(text("Created case of TestData"));
    table.valueAt(0, 1).contains("running");
  }
}
