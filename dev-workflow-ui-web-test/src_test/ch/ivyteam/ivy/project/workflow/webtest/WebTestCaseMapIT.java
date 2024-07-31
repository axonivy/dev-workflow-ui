package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestCaseMap;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.readonly;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;

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
    openView("cases.xhtml");
    $(".detail-btn").shouldBe(visible).click();

    $(By.id("creatorUser:userName")).shouldBe(exactText("DeveloperTest"));
    $(By.id("caseState")).shouldBe(exactText("OPEN (RUNNING)"));

    $(".case-map-column").shouldHave(text("stage1"));
    $(".process-flow").shouldBe(visible);
  }

  @Test
  void sidestepsCaseDetails() {
    openView("cases.xhtml");
    $(".detail-btn").shouldBe(visible).click();

    $(".current-hierarchy-case").find("a").shouldNotHave(text("Created case of TestData"));

    $(By.id("sidestepsBtn")).shouldBe(visible).click();
    $(By.id("sidestepMenu")).shouldBe(visible).find(By.className("ui-menuitem-link")).click();

    $$(".case-link").find(text("Created case of TestData")).shouldBe(visible);
  }

  @Test
  void sidestepsTaskDetails() {
    openView("tasks.xhtml");
    $(".detail-btn").shouldBe(visible).click();

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
    openView("cases.xhtml");
    $(By.className("detail-btn")).shouldBe(visible).click();
    $(By.className("current-hierarchy-case")).find("a").shouldNotHave(text("Created case of TestData"));
    $(By.className("casemap-card")).shouldBe(visible);
    $(By.id("openCaseMapUiViewerBtn")).shouldBe(visible).click();
    $(By.id("processViewer:processViewerDialog")).shouldBe(visible);
    $(By.id("viewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("viewerFrame");
    $(By.className("fa-apple")).shouldBe(visible);
    assertThat($(By.id("name-id")).getAttribute("value")).contains("test _ case _ map");
    $(By.id("name-id")).shouldBe(readonly);
  }
}
