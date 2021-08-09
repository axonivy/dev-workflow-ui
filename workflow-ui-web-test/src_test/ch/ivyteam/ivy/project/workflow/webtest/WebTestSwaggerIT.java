package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestSwaggerIT {

  @Test
  public void testSwaggerWorking() {
    Selenide.open(viewUrl("api-browser.xhtml"));
    if ($(By.id("apiBrowser")).is(visible)) {
      Selenide.switchTo().frame("apiBrowser");
    }
    $(".computed-url").shouldBe(visible);
    $(".computed-url").shouldHave(text("http"));
  }

}
