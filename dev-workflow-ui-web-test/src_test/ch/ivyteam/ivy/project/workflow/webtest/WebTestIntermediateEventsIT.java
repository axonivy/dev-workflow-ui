package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;

@IvyWebTest
class WebTestIntermediateEventsIT {

  @BeforeAll
  static void setup() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    startTestProcess("1750C5211D94569D/testIntermediateEventProcess.ivp");
  }

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
  }

  @Test
  void checkIntermediateElementDetails() {
    openView("intermediate-events.xhtml");
    $(byText("TestIntermediateEvent")).click();
    $(By.id("name")).shouldHave(text("TestIntermediateEvent"));
    $(By.id("description")).shouldHave(text("intermediate event description"));
  }

  @Test
  void checkIntermediateElementEventsTable() {
    openView("intermediate-events.xhtml");
    $(byText("TestIntermediateEvent")).click();
    $(By.id("id")).shouldBe(visible);
    var eventsTable = PrimeUi.table(By.id("eventsTable"));
    eventsTable.valueAtShouldBe(0, 6, text("NOTHING"));
  }
}
