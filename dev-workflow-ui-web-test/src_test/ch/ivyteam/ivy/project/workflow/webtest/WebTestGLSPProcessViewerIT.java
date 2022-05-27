package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestGLSPProcessViewerIT {

  @BeforeAll
  public static void setup() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @Test
  public void testOpenViewer() {
    loginDeveloper();
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:globalFilter")).sendKeys("testdata/testdata.ivp");
    $$(By.className("si-hierarchy-6")).shouldBe(size(1)).get(0).click();

    $(By.id("viewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("viewerFrame");
    $(By.id("sprotty_1750C5211D94569D-f0")).shouldBe(visible, text("TestData"));
    $$(By.className("start:requestStart")).shouldBe(sizeGreaterThanOrEqual(1));
    $$(By.className("sprotty-edge")).shouldBe(sizeGreaterThanOrEqual(1));
    $$(By.className("end:taskEnd")).shouldBe(sizeGreaterThanOrEqual(1));
  }

  @Test
  public void testCaseMapUiViewer() {
    loginDeveloper();
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:globalFilter")).sendKeys("test _ case _ map");
    $(By.className("si-hierarchy-6")).shouldBe(visible).click();
    $(By.id("startsForm:processViewer:processViewerDialog")).shouldBe(visible);
    $(By.id("viewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("viewerFrame");
    $(By.className("fa-apple")).shouldBe(visible);
    assertThat($(By.id("name-id")).getAttribute("value")).contains("test _ case _ map");
  }

  @Test
  public void testNoViewerForUnknownUser() {
    Selenide.open(viewUrl("starts.xhtml"));
    logout();
    $(By.id("startsForm:globalFilter")).sendKeys("testdata/testdata.ivp");
    $(By.className("si-hierarchy-6")).shouldNotBe(visible);
  }
}
