package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
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
    $(By.id("signalForm:signalBtn")).shouldBe(enabled);
  }

  @Test
  public void testSendSignals()
  {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();

    open(viewUrl("signals.xhtml"));
    Table signalsTable = PrimeUi.table(By.id("signalForm:firedSignalsTable"));
    signalsTable.containsNot("Web Test Signal");

    $(By.id("signalForm:signalCodeInput_input")).sendKeys("Web Test Signal");
    $(By.id("signalForm:signalBtn")).shouldBe(enabled).click();
    $(By.id("signalForm:growl_container")).shouldBe(visible);

    signalsTable.valueAtShoudBe(0, 1, text("Web Test Signal"));
  }

  @Test
  public void testSignalAutocomplete()
  {
    open(viewUrl("signals.xhtml"));
    $(By.id("signalForm:signalCodeInput")).findElement(By.tagName("button")).click();

    $(By.id("signalForm:signalCodeInput_input")).sendKeys("signal");
    $(By.id("signalForm:signalCodeInput_panel")).shouldNotHave(text("airport"));
    $(By.id("signalForm:signalCodeInput_panel")).findElement(By.className("ui-autocomplete-item")).click();
    $(By.id("signalForm:signalBtn")).shouldBe(enabled).click();

    Table signalsTable = PrimeUi.table(By.id("signalForm:firedSignalsTable"));
    signalsTable.valueAtShoudBe(0, 1, text("test:signal:complete"));
  }

  @Test
  public void testBoundarySignals()
  {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();

    open(viewUrl("signals.xhtml"));
    Table boundaryTable = PrimeUi.table(By.id("signalForm:boundarySignalsTable"));
    boundaryTable.valueAtShoudBe(0, 0, text("No records found."));

    startTestProcess("1750C5211D94569D/startBoundarySignal.ivp");
    open(viewUrl("signals.xhtml"));
    boundaryTable.valueAtShoudBe(0, 1, text("test:data:signal"));

    $(By.id("signalForm:signalCodeInput_input")).sendKeys("test:data:signal");
    $(By.id("signalForm:signalBtn")).shouldBe(enabled).click();
    boundaryTable.valueAtShoudBe(0, 0, text("No records found."));
  }

  @Test
  public void testBoundarySignalsOnClick()
  {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();

    open(viewUrl("signals.xhtml"));
    Table boundaryTable = PrimeUi.table(By.id("signalForm:boundarySignalsTable"));
    boundaryTable.valueAtShoudBe(0, 0, text("No records found."));

    startTestProcess("1750C5211D94569D/startBoundarySignal.ivp");
    open(viewUrl("signals.xhtml"));

    $(By.id("signalForm:boundarySignalsTable:0:sendSignalIcon")).shouldBe(visible).click();
    boundaryTable.valueAtShoudBe(0, 0, text("No records found."));
  }
}
