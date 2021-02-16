package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestCasesIT
{

  @Test
  public void allCasesOnlyAdmin()
  {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginFromTable("testuser");
    Selenide.open(viewUrl("cases.xhtml"));
    $(By.id("casesForm:allCasesSwitch")).shouldNotBe(visible);
    loginDeveloper();
    Selenide.open(viewUrl("cases.xhtml"));
    $(By.id("casesForm:allCasesSwitch")).shouldBe(visible);
  }

  @Test
  public void testCasesTable() throws Exception
  {
    open(viewUrl("home.xhtml"));
    loginDeveloper();

    startTestProcess("1750C5211D94569D/TestData.ivp");
    open(viewUrl("cases.xhtml"));
    Table table = PrimeUi.table(By.id("casesForm:cases"));
    table.firstRowContains("TestCase");

    table.valueAt(0, 1).contains("running");
  }

  @Test
  public void chceckCaseDetails()
  {
    open(viewUrl("home.xhtml"));
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    open(viewUrl("cases.xhtml"));

    $(".si-information-circle").shouldBe(visible).click();
    $("#form\\:caseName").shouldBe(text("TestCase"));

    $("#form\\:creatorUser").shouldBe(exactText("DeveloperTest"));
    $("#form\\:caseState").shouldBe(exactText("RUNNING"));

    $("#form\\:customFieldsContainer").shouldHave(text("test field = test value"));
    $("#form\\:documentsContainer").shouldHave(text("test.txt"));

    $("#form\\:caseDestroyBtn").should(visible).click();
    $("#form\\:caseState").shouldBe(exactText("DESTROYED"));
  }
}
