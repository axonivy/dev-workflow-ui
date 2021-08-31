package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestCaseMap;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestCaseMapIT {

  @BeforeAll
  static void setup() {
    Selenide.closeWebDriver();
    Configuration.proxyEnabled = true;
    Configuration.fileDownload = FileDownloadMode.PROXY;
  }

  @BeforeEach
  void beforeEach() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();
    startTestCaseMap("0cf1f054-a4ad-4b2b-bcf1-c9c34ec0a2ab.icm");
    $(By.id("form:proceed")).shouldBe(visible).click();
  }

  @Test
  public void caseDetails() {
    startTestCaseMap("0cf1f054-a4ad-4b2b-bcf1-c9c34ec0a2ab.icm");
    open(viewUrl("cases.xhtml"));
    $(".si-information-circle").shouldBe(visible).click();

    $("#form\\:creatorUser\\:userName").shouldBe(exactText("DeveloperTest"));
    $("#form\\:caseState").shouldBe(exactText("CREATED"));

    $(".case-map-column").shouldHave(text("stage1"));
    $(".process-flow").shouldBe(visible);
  }

  @Test
  public void sidestepsCaseDetails() {
    open(viewUrl("cases.xhtml"));
    $(".si-information-circle").shouldBe(visible).click();

    $(".current-hierarchy-case").find("a").shouldNotHave(text("TestCase"));

    $(By.id("form:sidestepsBtn")).shouldBe(visible).click();
    $(By.id("form:sidestepMenu")).shouldBe(visible).find(By.className("ui-menuitem-link")).click();

    $$(".case-link").find(text("TestCase")).shouldBe(visible);
  }

  @Test
  public void sidestepsTaskDetails() {
    open(viewUrl("allTasks.xhtml"));
    $(".si-information-circle").shouldBe(visible).click();

    $(By.id("form:sidestepsBtn")).shouldBe(visible).click();
    $(By.id("form:sidestepMenu")).shouldBe(visible).find(By.className("ui-menuitem-link")).click();
    $(By.id("form:sidestepsBtn")).shouldBe(visible);

    $("#form\\:taskStartBtn").shouldNotHave(cssClass("ui-state-disabled"));
    $("#form\\:taskStartBtn").shouldBe(enabled).click();
    $("#form\\:taskStartBtn").shouldHave(cssClass("ui-state-disabled"));
    $(By.id("form:sidestepsBtn")).shouldNotBe(visible);
  }
}
