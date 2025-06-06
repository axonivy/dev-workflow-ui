package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;

@IvyWebTest
class WebTestSignalsIT {

  @BeforeAll
  static void setup() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
    openView("signals.xhtml");
  }

  @Test
  void sendSignals() {
    $(By.id("signalForm:signalCodeInput_input")).setValue("Web Test Signal");
    $(By.id("signalForm:signalBtn")).shouldBe(enabled).click();
    $(By.id("signalForm:growl_container")).shouldBe(visible);

    var signalsTable = PrimeUi.table(By.id("firedSignalsTable"));
    signalsTable.valueAtShouldBe(0, 0, text("Web Test Signal"));
  }

  @Test
  void signalAutocomplete() {
    $(By.id("signalForm:signalCodeInput")).findElement(By.tagName("button")).click();

    $(By.id("signalForm:signalCodeInput_input")).setValue("signal");
    $(By.id("signalForm:signalCodeInput_panel")).shouldNotHave(text("airport"));
    $(By.id("signalForm:signalCodeInput_panel")).findElement(By.className("ui-autocomplete-item")).click();
    $(By.id("signalForm:signalBtn")).shouldBe(enabled).click();

    var signalsTable = PrimeUi.table(By.id("firedSignalsTable"));
    signalsTable.valueAtShouldBe(0, 0, text("test:signal:complete"));
  }

  @Test
  void caseProcessViewerCanOpen() {
    $(By.id("signalForm")).shouldBe(visible);
    $(By.id("signalForm:signalCodeInput")).findElement(By.tagName("button")).click();

    $(By.id("signalForm:signalCodeInput_input")).setValue("test:signal:complete");
    $(By.id("signalForm:signalBtn")).shouldBe(enabled).click();

    var signalsTable = PrimeUi.table(By.id("firedSignalsTable"));
    signalsTable.valueAtShouldBe(0, 0, text("test:signal:complete"));
    signalsTable.valueAtShouldBe(0, 4, text("test:signal:complete"));

    $(By.className("case-link")).shouldBe(visible).click();
    $(By.id("processViewer:processViewerDialog")).shouldNotBe(visible);
    $(By.id("openProcessViewerBtn")).shouldBe(visible).click();
    $(By.id("processViewer:processViewerDialog")).shouldBe(visible);
  }

  @Test
  void boundarySignals() {
    var boundaryTable = PrimeUi.table(By.id("boundarySignalsTable"));
    boundaryTable.contains("No signals found");

    startTestProcess("1750C5211D94569D/startBoundarySignal.ivp");
    openView("signals.xhtml");
    boundaryTable.valueAtShouldBe(0, 0, text("test:data:signal"));

    $(By.id("signalForm:signalCodeInput_input")).setValue("test:data:signal");
    $(By.id("signalForm:signalBtn")).shouldBe(enabled).click();
    boundaryTable.valueAtShouldBe(0, 0, text("No signals found"));
  }

  @Test
  void boundarySignalsOnClick() {
    var boundaryTable = PrimeUi.table(By.id("boundarySignalsTable"));
    boundaryTable.contains("No signals found");

    startTestProcess("1750C5211D94569D/startBoundarySignal.ivp");
    openView("signals.xhtml");

    $(By.id("boundarySignalsTable:0:sendSignalIcon")).shouldBe(visible).click();
    boundaryTable.contains("No signals found");
  }
}
