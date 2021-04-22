package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;

@IvyWebTest
public class WebTestSignalsIT
{
  @Test
  public void testSignalAdminOnly()
  {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginFromTable("testuser");
    open(viewUrl("signals.xhtml"));
    $(By.id("signalForm:signalBtn")).shouldBe(disabled);

    loginDeveloper();
    open(viewUrl("signals.xhtml"));
    $(By.id("signalForm:signal-code-input_input")).sendKeys("Test Code");
    $(By.id("signalForm:signalBtn")).shouldBe(enabled).click();
    $(By.id("signalForm:growl_container")).shouldBe(visible);

    Table signalsTable = PrimeUi.table(By.id("signalForm:fired-signals-table"));
    signalsTable.valueAt(0, 0).contains("Test Code");
  }
}
