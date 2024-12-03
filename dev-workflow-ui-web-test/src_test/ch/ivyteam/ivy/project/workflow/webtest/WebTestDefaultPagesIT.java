package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlContains;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestDefaultPagesIT {

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
    openView("starts.xhtml");
    var table = PrimeUi.table(By.id("startsForm:projectStarts"));
    table.searchGlobal("testDefaultPages");
    table.contains("dev-workflow-ui-test-data");
    table.row(0).shouldHave(text("TestData/testDefaultPages.ivp")).find(".start-name").click();
    $(By.id("iFrame")).shouldBe(visible);
    Selenide.switchTo().frame("iFrame");
    $(By.id("defaultPagesTitle")).shouldBe(visible);
  }

  @Test
  public void testRedirectDefaultHome() {
    $(By.id("homeBtn")).shouldBe(visible).click();
    Selenide.switchTo().defaultContent();
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-nav-page"));
    assertCurrentUrlContains("home.xhtml");
  }

  @Test
  public void testRedirectDefaultProcessStarts() {
    $(By.id("startsBtn")).shouldBe(visible).click();
    Selenide.switchTo().defaultContent();
    $(By.id("menuform:sr_starts")).shouldHave(cssClass("active-nav-page"));
    assertCurrentUrlContains("starts.xhtml");
  }

  @Test
  public void testRedirectDefaultTaskList() {
    $(By.id("tasksBtn")).shouldBe(visible).click();
    Selenide.switchTo().defaultContent();
    $(By.id("menuform:sr_tasks")).shouldHave(cssClass("active-nav-page"));
    assertCurrentUrlContains("tasks.xhtml");
  }

  @Test
  public void testRedirectDefaultLoginPage() {
    $(By.id("loginBtn")).shouldBe(visible).click();
    Selenide.switchTo().defaultContent();
    $(By.id("loginForm")).shouldBe(visible);
    assertCurrentUrlContains("login.xhtml");
  }
}
