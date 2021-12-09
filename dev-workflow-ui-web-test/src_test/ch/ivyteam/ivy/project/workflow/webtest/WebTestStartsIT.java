package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlEndsWith;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.codeborne.selenide.Selenide;

import ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil;

@IvyWebTest
public class WebTestStartsIT {

  @BeforeAll
  public static void prepare() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @BeforeEach
  public void loginAdmin() {
    Selenide.switchTo().defaultContent();
    WorkflowUiUtil.loginDeveloper();
  }

  @Test
  public void testFilter() {
    startTestProcess("1750C5211D94569D/TestData.ivp");
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:projectStarts")).shouldBe(visible);
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui"));
    $(By.id("startsForm:filter")).sendKeys("makeAdmin");
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui-test-data"));
    $(By.id("startsForm:projectStarts")).shouldNotHave(exactText("workflow-ui"));
  }

  @Test
  public void testExecuteStart() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).sendKeys("startTestDialog1");
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui-test-data"));
    $(By.className("start-link")).shouldBe(visible).shouldHave(text("startTestDialog1.ivp")).click();
    $(By.id("iFrame")).shouldBe(visible);

    Selenide.switchTo().frame("iFrame");
    $(By.id("testDialogTitle")).shouldBe(visible);
    $(By.id("form:testInput")).sendKeys("test input");
    $(By.id("form:testSelectOneMenu")).shouldBe(visible).click();
    $(By.id("form:testSelectOneMenu_2")).shouldBe(visible).click();
    $(By.id("form:proceed")).shouldBe(visible).click();
    assertCurrentUrlEndsWith("starts.xhtml");
  }

  @Test
  public void startNotEmbedInFrame() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).sendKeys("embed in frame");
    // open in fullscreen link icon shouldn't be visible
    $(By.className("start-element")).findAll("a").shouldBe(size(1));
    $(By.className("start-link")).shouldBe(visible, text("Do not embed in Frame")).click();
    $(By.id("testDialogTitle")).shouldBe(visible);
  }

  @Test
  public void testExecuteDefaultFramePage() {
    Selenide.open(EngineUrl
            .createProcessUrl("/dev-workflow-ui-test-data/1750C5211D94569D/startTestDialog1.ivp?embedInFrame"));
    if ($(By.id("iFrame")).is(visible)) {
      Selenide.switchTo().frame("iFrame");
    }
    $(By.id("form:testInput")).sendKeys("test input");
    $(By.id("form:testSelectOneMenu")).shouldBe(visible).click();
    $(By.id("form:testSelectOneMenu_1")).shouldBe(visible).click();
  }

  @Test
  public void testWebservicesVisible() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).sendKeys("testservice");
    $(By.id("startsForm:projectStarts")).shouldHave(text("TestService"));
  }

  @Test
  public void testRedirectWhenFinished() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:projectStarts")).shouldBe(visible);
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui"));
    $(By.id("startsForm:filter")).sendKeys("testData");
    $(By.id("startsForm:projectStarts")).shouldHave(text("workflow-ui-test-data"));
    $(byText("TestData.ivp")).shouldBe(visible).click();

    open(viewUrl("tasks.xhtml"));
    $(By.id("tasksForm:tasks")).find("TestTask");
    $(By.id("menuform:sr_tasks")).shouldHave(cssClass("active-menu"));
    assertCurrentUrlEndsWith("tasks.xhtml");

    open(viewUrl("home.xhtml"));
    $(By.id("form:lastStarts")).find(byText("TestData.ivp")).click();
    $(By.id("menuform:sr_home")).shouldHave(cssClass("active-menu"));
    assertCurrentUrlEndsWith("home.xhtml");

    open(viewUrl("allTasks.xhtml"));
    $(".si-information-circle").shouldBe(visible).click();
    $(".case-link").shouldHave(text("Created case of TestData"));
    var taskId = $("#form\\:taskId").getText();
    $("#form\\:taskStartBtn").shouldBe(enabled).click();
    assertCurrentUrlEndsWith("taskDetails.xhtml?task="+taskId);
  }

  @Test
  public void testExecuteOnFullscreenPage() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).sendKeys("startTestDialog1");
    $(byText("startTestDialog1.ivp")).shouldBe(visible);
    $(By.className("si-expand-6")).shouldBe(visible).click();
    $(By.className("topbar-logo")).shouldNotBe(visible);
    $(By.id("form:proceed")).shouldBe(visible).click();
    $(By.className("topbar-logo")).shouldBe(visible);
  }

  @Test
  public void testFrameHeaderBarSidestep() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).sendKeys("test _ case _ map");
    $(byText("test _ case _ map")).shouldBe(visible).click();
    $(By.id("iFrameForm:frameTaskName")).shouldHave(text("Test Developer Workflow-UI Dialog 1"));
    $(By.id("iFrameForm:sidestepsBtn")).shouldBe(visible).click();
    $(By.id("iFrameForm:sidestepMenu")).shouldBe(visible).find(By.className("ui-menuitem-link")).click();
    $(By.id("iFrameForm:frameTaskName")).shouldHave(text("Test Developer Workflow-UI Dialog 2"));
    Selenide.switchTo().frame("iFrame");
    $(By.id("form:proceed")).shouldBe(enabled).click();
    Selenide.switchTo().defaultContent();
    $(By.id("menuform:sr_starts")).shouldHave(cssClass("active-menu"));
  }

  @Test
  public void testStartableIcons() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).setValue("makeAdminUser.ivp");
    $(By.id("startableIcon")).shouldBe(visible).shouldHave(cssClass("si-controls-play"));
    $(By.id("startsForm:filter")).setValue("HomePageTestData.ivp");
    $(By.id("startableIcon")).shouldBe(visible).shouldHave(cssClass("si-house-1"));
  }
}
