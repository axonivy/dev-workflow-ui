package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlContains;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.open;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.codeborne.selenide.Selenide;

import ch.ivyteam.ivy.project.workflow.webtest.util.Navigation;

@IvyWebTest
class WebTestStartsIT {

  @BeforeAll
  static void prepare() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @BeforeEach
  void loginAdmin() {
    Selenide.switchTo().defaultContent();
    loginDeveloper();
  }

  @Test
  void filter() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("starts.xhtml");
    var starts = PrimeUi.table(By.id("startsForm:projectStarts"));
    starts.searchGlobal("makeAdmin");
    starts.row(0).shouldHave(text("makeAdmin"), text("workflow-ui-test-data"));
  }

  @Test
  void executeStart() {
    openView("starts.xhtml");
    var starts = PrimeUi.table(By.id("startsForm:projectStarts"));
    starts.searchGlobal("startTestDialog1");
    starts.contains("workflow-ui-test-data");
    starts.row(0).shouldHave(text("startTestDialog1")).find(".start-name").click();
    $(By.id("iFrame")).shouldBe(visible);

    Selenide.switchTo().frame("iFrame");
    $(By.id("testDialogTitle")).shouldBe(visible);
    $(By.id("form:testInput")).setValue("test input");
    $(By.id("form:testSelectOneMenu")).shouldBe(visible).click();
    $(By.id("form:testSelectOneMenu_2")).shouldBe(visible).click();
    $(By.id("form:proceed")).shouldBe(visible).click();
    assertCurrentUrlContains("home.xhtml");
  }

  @Test
  void startProcessByUrlParameter() {
    var app = EngineUrl.applicationName();
    var pmv = "dev-workflow-ui-test-data$1";
    var startableId = EngineUrl.applicationName() + "/dev-workflow-ui-test-data/TestData/startTestDialog1.ivp";
    open(viewUrl("start.xhtml", Map.of("app", app, "pmv", pmv, "id", startableId)));
    assertCurrentUrlContains("frame.xhtml?");
    Selenide.switchTo().frame("iFrame");
    $(By.id("testDialogTitle")).shouldBe(visible);
    $(By.id("form:testSelectOneMenu")).shouldBe(visible).click();
    $(By.id("form:proceed")).shouldBe(visible).click();
    assertCurrentUrlContains("home.xhtml");
  }

  @Test
  void checkIsNonVisibleStartEventStartable() {
    var app = EngineUrl.applicationName();
    var pmv = "dev-workflow-ui-test-data$1";
    var startableId = EngineUrl.applicationName() + "/dev-workflow-ui-test-data/TestData/nonVisibleStartTestDialog3.ivp";
    open(viewUrl("start.xhtml", Map.of("app", app, "pmv", pmv, "id", startableId)));
    assertCurrentUrlContains("frame.xhtml?");
    Selenide.switchTo().frame("iFrame");
    $(By.id("testDialogTitle")).shouldBe(visible);
    $(By.id("form:proceed")).shouldBe(visible).click();
    assertCurrentUrlContains("home.xhtml");
  }

  @Test
  void startProcessByWrongUrlParameterId() {
    var wrongStartId = "this_is_something_that_does_not_exist31576%2F1337.ivp";
    open(viewUrl("start.xhtml", Map.of("id", wrongStartId)));
    assertCurrentUrlContains("start.xhtml?id=" + wrongStartId);
  }

  @Test
  void startNotEmbedInFrame() {
    openView("starts.xhtml");
    var starts = PrimeUi.table(By.id("startsForm:projectStarts"));
    starts.searchGlobal("embed in frame");
    starts.contains("workflow-ui-test-data");
    var start = starts.row(0).shouldHave(text("Do not embed in Frame"));
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    // open in fullscreen link icon shouldn't be visible
    $(By.id("startsForm:projectStarts:0:openStartFullscreenBtn")).shouldNotBe(visible);
    start.find(".start-name").click();
    $(By.id("testDialogTitle")).shouldBe(visible);
  }

  @Test
  void executeDefaultFramePage() {
    var url = EngineUrl.create()
        .process("/dev-workflow-ui-test-data/1750C5211D94569D/startTestDialog1.ivp")
        .queryParam("embedInFrame", "")
        .toUrl();
    Selenide.open(url);
    if ($(By.id("iFrame")).is(visible)) {
      Selenide.switchTo().frame("iFrame");
    }
    $(By.id("form:testInput")).setValue("test input");
    $(By.id("form:testSelectOneMenu")).shouldBe(visible).click();
    $(By.id("form:testSelectOneMenu_1")).shouldBe(visible).click();
  }

  @Test
  void webservicesVisible() {
    openView("webservices.xhtml");
    var table = PrimeUi.table(By.id("webServicesForm:webServices"));
    table.searchGlobal("testservice");
    table.contains("TestService");
  }

  @Test
  void redirectWhenFinished() {
    openView("starts.xhtml");
    var starts = PrimeUi.table(By.id("startsForm:projectStarts"));
    starts.searchGlobal("TestData/TestData.ivp");
    starts.row(0).shouldHave(text("TestData/TestData.ivp")).find(".start-name").click();

    openView("tasks.xhtml");
    $(By.id("tasksForm:tasks")).find("TestTask");
    $(By.id("menuform:sr_tasks")).shouldHave(cssClass("active-nav-page"));
    assertCurrentUrlContains("tasks.xhtml");

    openView("home.xhtml");
    $(By.id("startedProcesses")).find(byText("TestData/TestData.ivp")).click();
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-nav-page"));
    assertCurrentUrlContains("home.xhtml");

    Navigation.openTask("Created task of TestData");
    $(".case-link").shouldHave(text("Created case of TestData"));
    $("#actionMenuForm\\:taskStartBtn").shouldBe(enabled).click();
    $(".case-link").shouldHave(text("Created case of TestData"));
  }

  @Test
  void executeOnFullscreenPage() {
    openView("starts.xhtml");
    var starts = PrimeUi.table(By.id("startsForm:projectStarts"));
    starts.searchGlobal("startTestDialog1");
    starts.row(0).shouldHave(text("startTestDialog1"));
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:openStartFullscreenBtn")).shouldBe(visible).click();
    $(By.id("topbar-logo")).shouldNotBe(visible);
    $(By.id("form:proceed")).shouldBe(visible).click();
    $(By.className("layout-topbar-logo")).shouldBe(visible);
  }

  @Test
  void frameHeaderBarSidestep() {
    openView("starts.xhtml");
    var starts = PrimeUi.table(By.id("startsForm:projectStarts"));
    starts.searchGlobal("test _ case _ map");
    starts.row(0).shouldHave(text("test _ case _ map")).find(".start-name").click();
    $(By.id("iFrameForm:frameTaskName")).shouldHave(text("Test Developer Workflow-UI Dialog 1"));
    $(By.id("iFrameForm:sidestepsBtn")).shouldBe(visible).click();
    $(By.id("iFrameForm:sidestepMenu")).shouldBe(visible).find(By.className("ui-menuitem-link")).click();
    $(By.id("iFrameForm:frameTaskName")).shouldHave(text("Test Developer Workflow-UI Dialog 2"));
    Selenide.switchTo().frame("iFrame");
    $(By.id("form:proceed")).shouldBe(enabled).click();
    Selenide.switchTo().defaultContent();
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-nav-page"));
  }

  @Test
  void startableIcons() {
    openView("starts.xhtml");
    var starts = PrimeUi.table(By.id("startsForm:projectStarts"));
    starts.searchGlobal("makeAdminUser.ivp");
    $(By.id("startsForm:projectStarts:0:processStartIcon")).shouldBe(visible).shouldHave(cssClass("si-controls-play"));
    starts.searchGlobal("HomePageTestData.ivp");
    $(By.id("startsForm:projectStarts:0:processStartIcon")).shouldBe(visible).shouldHave(cssClass("si-house-1"));
  }
}
