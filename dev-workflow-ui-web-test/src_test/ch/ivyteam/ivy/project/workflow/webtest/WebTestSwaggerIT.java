package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;

import ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil;

@IvyWebTest
public class WebTestSwaggerIT {

  @Test
  public void testSwaggerWorking() {
    WorkflowUiUtil.loginDeveloper();
    $(By.id("menuform:sr_actions")).shouldBe(visible);
    Selenide.open(viewUrl("api-browser.xhtml"));
    $(By.id("apiBrowser")).shouldBe(visible);
    Selenide.switchTo().frame("apiBrowser");
    $(By.className("computed-url")).shouldBe(visible);
    $(By.className("computed-url")).shouldHave(text("http"));
  }

}
