package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;

@IvyWebTest
public class WebTestCasesIT {

  @Test
  public void allCasesOnlyAdmin() {
    loginFromTable("testuser");

    openView("cases.xhtml");
    $(By.id("casesForm:allCasesSwitch")).shouldNotBe(visible);
    loginDeveloper();
    openView("cases.xhtml");
    $(By.id("casesForm:allCasesSwitch")).shouldBe(visible);
  }

  @Test
  public void testCasesTable() throws Exception {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();

    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("cases.xhtml");
    Table table = PrimeUi.table(By.id("casesForm:cases"));
    table.row(0).shouldBe(text("Created case of TestData"));

    table.valueAt(0, 1).contains("running");
  }

}
