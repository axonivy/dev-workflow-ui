package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestGLSPProcessViewerIT {

  @BeforeAll
  public static void setup() {
    Configuration.proxyEnabled = false;
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @Test
  public void testOpenViewer() {
    loginDeveloper();
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("testdata/testdata.ivp");
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:openProcessViewer")).shouldBe(visible).click();

    $(By.id("viewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("viewerFrame");
    $("#sprotty_ivy-viewport-bar").shouldBe(visible, Duration.ofSeconds(60));
    $("#sprotty_1750C5211D94569D-f0").shouldBe(visible);
    $$(By.className("start:requestStart")).shouldBe(sizeGreaterThanOrEqual(1));
    $$(By.className("sprotty-edge")).shouldBe(sizeGreaterThanOrEqual(1));
    $$(By.className("end:taskEnd")).shouldBe(sizeGreaterThanOrEqual(1));
  }

  @Test
  public void testCaseMapUiViewer() {
    loginDeveloper();
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("test _ case _ map");
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:startsActionsMenu")).shouldBe(visible);
    $(By.id("startsForm:projectStarts:0:openProcessViewer")).shouldBe(visible).click();
    $(By.id("startsForm:processViewer:processViewerDialog")).shouldBe(visible);
    $(By.id("viewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("viewerFrame");
    $(By.className("fa-apple")).shouldBe(visible);
    assertThat($(By.id("name-id")).getAttribute("value")).contains("test _ case _ map");
  }

  @Test
  public void testNoViewerForUnknownUser() {
    logout();
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:openProcessViewer")).shouldNot(exist);
  }
}
