package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestHomeRedirect {

  @Test
  public void homepageRedirect() {
    Selenide.open(viewUrl("home.xhtml"));
    $(By.id("menuform:sr_starts")).shouldHave(cssClass("active-menu"));
    Selenide.open(viewUrl("home.xhtml"));
    $(By.id("menuform:sr_starts")).shouldNotHave(cssClass("active-menu"));
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-menu"));
  }
}
