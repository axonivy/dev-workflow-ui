package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;

@IvyWebTest
public class WebTestHomeRedirect {

  @Test
  public void homepageRedirect() {
    loginFromTable("testuser");
    startTestProcess("1750C5211D94569D/cleanupSession.ivp");

    open(viewUrl("home.xhtml"));
    $(By.id("menuform:sr_starts")).shouldHave(cssClass("active-menuitem"));
    open(viewUrl("home.xhtml"));
    $(By.id("menuform:sr_starts")).shouldNotHave(cssClass("active-menuitem"));
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-menuitem"));
  }
}
