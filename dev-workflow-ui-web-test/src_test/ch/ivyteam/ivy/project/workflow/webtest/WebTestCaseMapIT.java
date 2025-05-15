package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestCaseMap;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.readonly;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;

import ch.ivyteam.ivy.project.workflow.webtest.util.Navigation;

@IvyWebTest
class WebTestCaseMapIT {

  @BeforeAll
  static void setup() {
    Selenide.closeWebDriver();
  }

  @BeforeEach
  void beforeEach() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();
    startTestCaseMap();
  }

  @Test
  void caseDetails() {
    Navigation.openCase("startTestDialog1");
    $(By.id("creatorUser:userName")).shouldBe(exactText("DeveloperTest"));
    $(By.id("caseState:stateBadge")).hover();
    $(By.id("caseState:tooltip")).$(".ui-tooltip-text").shouldHave(text("RUNNING"));

    $(".case-map-column").shouldHave(text("stage1"));
    $(".process-flow").shouldBe(visible);
  }

  @Test
  void sidestepsCaseDetails() {
    Navigation.openCase("startTestDialog1");
    $(".current-hierarchy-case").find("a").shouldNotHave(text("Created case of TestData"));

    $(By.id("sidestepsBtn")).shouldBe(visible).click();
    $(By.id("sidestepMenu")).shouldBe(visible).find(By.className("ui-menuitem-link")).click();

    $$(".case-link").find(text("Created case of TestData")).shouldBe(visible);
  }

  @Test
  void sidestepsTaskDetails() {
    Navigation.openTask("First task of");
    $(By.id("actionMenuForm:sidestepsBtn")).shouldBe(visible).click();
    $(By.id("actionMenuForm:sidestepMenu")).shouldBe(visible).find(By.className("ui-menuitem-link")).click();
    $(By.id("actionMenuForm:sidestepsBtn")).shouldBe(visible);

    $(By.id("actionMenuForm:taskStartBtn")).shouldNotHave(cssClass("ui-state-disabled"));
    $(By.id("actionMenuForm:taskStartBtn")).shouldBe(enabled).click();
    $(By.id("actionMenuForm:taskStartBtn")).shouldHave(cssClass("ui-state-disabled"));
    $(By.id("actionMenuForm:sidestepsBtn")).shouldNotBe(visible);
  }

  @Test
  void caseMapUi() {
    Navigation.openCase("startTestDialog1");
    $(By.className("current-hierarchy-case")).find("a").shouldNotHave(text("Created case of TestData"));
    $(By.className("casemap-card")).shouldBe(visible);
    $(By.id("openCaseMapUiViewerBtn")).shouldBe(visible).click();
    $(By.id("processViewer:processViewerDialog")).shouldBe(visible);
    $(By.id("viewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("viewerFrame");
    $(By.className("fa-apple")).shouldBe(visible);
    $(By.id("name-id")).should(readonly, value("test _ case _ map"));
  }
}
