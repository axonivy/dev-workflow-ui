package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlEndsWith;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestStartsIT
{

  @Test
  public void testFilter()
  {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:projectStarts")).shouldBe(visible);
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui"));
    $(By.id("startsForm:filter")).sendKeys("main/DefaultApplication");
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui"));
    $(By.id("startsForm:projectStarts")).shouldNotHave(text("workflow-ui-test-data"));
  }

  @Test
  public void testExecuteStart()
  {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).sendKeys("workflow-ui");
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui"));
    $(By.id("start-link")).shouldBe(visible).click();
    assertCurrentUrlEndsWith("home.xhtml");
  }
}
