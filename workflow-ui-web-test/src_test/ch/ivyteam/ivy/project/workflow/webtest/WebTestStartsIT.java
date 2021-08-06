package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlEndsWith;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestStartsIT {

  @Test
  public void testFilter() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:projectStarts")).shouldBe(visible);
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui"));
    $(By.id("startsForm:filter")).sendKeys("makeAdmin");
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui-test-data"));
    $(By.id("startsForm:projectStarts")).shouldNotHave(exactText("workflow-ui"));
  }

  @Test
  public void testExecuteStart() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).sendKeys("startTestDialog");
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui-test-data"));
    $(byText("startTestDialog.ivp")).shouldBe(visible).click();
    $(By.id("iFrame")).shouldBe(visible);
    assertCurrentUrlEndsWith("startTestDialog.ivp");

    Selenide.switchTo().frame("iFrame");
    $(By.id("form:testInput")).sendKeys("test input");
    $(By.id("form:testSelectOneMenu")).shouldBe(visible).click();
    $(By.id("form:testSelectOneMenu_2")).shouldBe(visible).click();
  }

  @Test
  public void testExecuteDefaultFramePage() {
    Selenide.open(EngineUrl
            .createProcessUrl("/workflow-ui-test-data/1750C5211D94569D/startTestDialog.ivp?embedInFrame"));
    if ($(By.id("iFrame")).is(visible)) {
      Selenide.switchTo().frame("iFrame");
    }
    $(By.id("form:testInput")).sendKeys("test input");
    $(By.id("form:testSelectOneMenu")).shouldBe(visible).click();
    $(By.id("form:testSelectOneMenu_1")).shouldBe(visible).click();
  }

  @Test
  public void testWebservicesVisible() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).sendKeys("testservice");
    $(By.id("startsForm:projectStarts")).shouldHave(text("TestService"));
  }
}
