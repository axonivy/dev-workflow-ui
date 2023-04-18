package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;

import ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil;

@IvyWebTest
public class WebTestHomeRedirect {

  @Test
  public void homepageRedirect() {
    WorkflowUiUtil.loginFromTable("testuser");
    WorkflowUiUtil.startTestProcess("1750C5211D94569D/cleanupSession.ivp");

    openView("home.xhtml");
    $(By.id("menuform:sr_starts")).shouldHave(cssClass("active-menuitem"));
    openView("home.xhtml");
    $(By.id("menuform:sr_starts")).shouldNotHave(cssClass("active-menuitem"));
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-menuitem"));
  }
}
