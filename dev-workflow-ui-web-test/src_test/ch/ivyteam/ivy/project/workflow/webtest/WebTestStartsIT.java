package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlContains;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestStartsIT {

  @BeforeAll
  public static void prepare() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @BeforeEach
  public void loginAdmin() {
    Selenide.switchTo().defaultContent();
    loginDeveloper();
  }

  @Test
  public void testFilter() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts")).shouldBe(visible);
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui"));
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("makeAdmin");
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui-test-data"));
    $(By.id("startsForm:projectStarts")).shouldNotHave(exactText("workflow-ui"));
  }

  @Test
  public void testExecuteStart() {
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("startTestDialog1");
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui-test-data"));
    $(By.id("startsForm:projectStarts:0:startName")).shouldBe(visible).click();
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
  public void startNotEmbedInFrame() {
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("embed in frame").pressEnter();
    // open in fullscreen link icon shouldn't be visible
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:openStartFullscreenBtn")).shouldNotBe(visible);
    $(By.id("startsForm:projectStarts:0:startName")).shouldBe(visible, text("Do not embed in Frame")).click();
    $(By.id("testDialogTitle")).shouldBe(visible);
  }

  @Test
  public void testExecuteDefaultFramePage() {
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
  public void testWebservicesVisible() {
    openView("webservices.xhtml");
    $(By.id("webServicesForm:webServices:globalFilter")).setValue("testservice");
    $(By.id("webServicesForm:webServices")).shouldHave(text("TestService"));
  }

  @Test
  public void testRedirectWhenFinished() {
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts")).shouldBe(visible);
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui"));
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("testData");
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui-test-data"));
    $(byText("TestData/TestData.ivp")).shouldBe(visible).click();

    openView("tasks.xhtml");
    $(By.id("tasksForm:tasks")).find("TestTask");
    $(By.id("menuform:sr_tasks")).shouldHave(cssClass("active-nav-page"));
    assertCurrentUrlContains("tasks.xhtml");

    openView("home.xhtml");
    $(By.id("startedProcesses")).find(byText("TestData/TestData.ivp")).click();
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-nav-page"));
    assertCurrentUrlContains("home.xhtml");

    openView("tasks.xhtml");
    $(By.id("tasksForm:tasks:0:taskName")).shouldBe(visible).click();
    $(".case-link").shouldHave(text("Created case of TestData"));
    $("#actionMenuForm\\:taskStartBtn").shouldBe(enabled).click();
  }

  @Test
  public void testExecuteOnFullscreenPage() {
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("startTestDialog1");
    $(byText("TestData/startTestDialog2.ivp")).shouldNotBe(visible);
    $(byText("TestData/startTestDialog1.ivp")).shouldBe(visible);
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:openStartFullscreenBtn")).shouldBe(visible).click();
    $(By.id("topbar-logo")).shouldNotBe(visible);
    $(By.id("form:proceed")).shouldBe(visible).click();
    $(By.className("layout-topbar-logo")).shouldBe(visible);
  }

  @Test
  public void testFrameHeaderBarSidestep() {
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("test _ case _ map");
    $(By.id("startsForm:projectStarts:0:startName")).shouldBe(visible).click();
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
  public void testStartableIcons() {
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("makeAdminUser.ivp");
    $(By.id("startsForm:projectStarts:0:processStartIcon")).shouldBe(visible).shouldHave(cssClass("si-controls-play"));
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("HomePageTestData.ivp");
    $(By.id("startsForm:projectStarts:0:processStartIcon")).shouldBe(visible).shouldHave(cssClass("si-house-1"));
  }
}
