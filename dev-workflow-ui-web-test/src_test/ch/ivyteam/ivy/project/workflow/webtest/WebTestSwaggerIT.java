package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;

@IvyWebTest
class WebTestSwaggerIT {

  @Test
  void swaggerWorking() {
    loginDeveloper();
    $(By.id("menuform:sr_actions")).shouldBe(visible);
    openView("api-browser.xhtml");
    $(By.id("apiBrowser")).shouldBe(visible);
    Selenide.switchTo().frame("apiBrowser");
    $(By.className("computed-url")).shouldBe(visible);
    $(By.className("computed-url")).shouldHave(text("http"));
  }

}
