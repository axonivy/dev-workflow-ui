package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.open;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;

@IvyWebTest
class WebTestHomeRedirect {

  @Test
  void homepageRedirect() {
    loginFromTable("testuser");
    startTestProcess("1750C5211D94569D/cleanupSession.ivp");

    open(viewUrl("home.xhtml"));
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-nav-page"));

    logout();
    open(viewUrl("home.xhtml"));
    $(By.id("loginTable")).shouldBe(visible);
  }
}
