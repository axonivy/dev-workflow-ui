package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;

@IvyWebTest
public class WebTestHomeRedirect {

  @Test
  public void homepageRedirect() {
    openView("home.xhtml");
    $(By.id("menuform:sr_starts")).shouldHave(cssClass("active-menu"));
    openView("home.xhtml");
    $(By.id("menuform:sr_starts")).shouldNotHave(cssClass("active-menu"));
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-menu"));
  }
}
