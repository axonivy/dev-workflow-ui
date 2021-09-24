package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.cssClass;
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
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestIntermediateEventsIT {
  @BeforeAll
  public static void setup() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    startTestProcess("1750C5211D94569D/testIntermediateEventProcess.ivp");
  }

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
  }

  @Test
  public void adminOnly() {
    Selenide.open(viewUrl("home.xhtml"));
    loginFromTable("testuser");
    Selenide.open(viewUrl("intermediateEvents.xhtml"));
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-menu"));

    loginDeveloper();
    Selenide.open(viewUrl("intermediateEvents.xhtml"));
    $(byText("TestIntermediateEvent")).click();
    $(By.id("intermediateElementDetailsForm:id")).shouldBe(visible);
  }

  @Test
  public void checkIntermediateElementDetails() {
    Selenide.open(viewUrl("intermediateEvents.xhtml"));
    $(byText("TestIntermediateEvent")).click();
    $(By.id("intermediateElementDetailsForm:name")).shouldBe(text("TestIntermediateEvent"));
    $(By.id("intermediateElementDetailsForm:description")).shouldBe(text("intermediate event description"));
  }

  @Test
  public void checkIntermediateElementEventsTable() {
    Selenide.open(viewUrl("intermediateEvents.xhtml"));
    $(byText("TestIntermediateEvent")).click();
    $(By.id("intermediateElementDetailsForm:id")).shouldBe(visible);

    Table eventsTable = PrimeUi.table(By.id("intermediateElementDetailsForm:eventsTable"));
    eventsTable.valueAtShouldBe(0, 0, text("1"));
    eventsTable.valueAtShouldBe(0, 6, text("NOTHING"));
  }

}
