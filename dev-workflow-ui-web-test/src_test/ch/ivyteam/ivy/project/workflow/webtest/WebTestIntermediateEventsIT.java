package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
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
    openView("home.xhtml");
    loginFromTable("testuser");
    openView("intermediateEvents.xhtml");
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-menu"));

    loginDeveloper();
    openView("intermediateEvents.xhtml");
    $(byText("TestIntermediateEvent")).click();
    $(By.id("id")).shouldBe(visible);
  }

  @Test
  public void checkIntermediateElementDetails() {
    openView("intermediateEvents.xhtml");
    $(byText("TestIntermediateEvent")).click();
    $(By.id("name")).shouldBe(text("TestIntermediateEvent"));
    $(By.id("description")).shouldBe(text("intermediate event description"));
  }

  @Test
  public void checkIntermediateElementEventsTable() {
    openView("intermediateEvents.xhtml");
    $(byText("TestIntermediateEvent")).click();
    $(By.id("id")).shouldBe(visible);

    var eventsTable = PrimeUi.table(By.id("eventsTable"));
    eventsTable.valueAtShouldBe(0, 6, text("NOTHING"));
  }

}
