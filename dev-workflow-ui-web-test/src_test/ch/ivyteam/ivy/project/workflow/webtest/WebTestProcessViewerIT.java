package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

@IvyWebTest
class WebTestProcessViewerIT {

  @BeforeAll
  static void setup() {
    Configuration.proxyEnabled = false;
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
  }

  @BeforeEach
  void init() {
    loginDeveloper();
    openView("starts.xhtml");
  }

  @Test
  void openViewer() {
    var table = new Table(By.id("startsForm:projectStarts"));
    table.searchGlobal("testdata/testdata.ivp");
    table.row(0).shouldHave(text("testdata.ivp"));
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
  void caseMapUiViewer() {
    var table = new Table(By.id("startsForm:projectStarts"));
    table.searchGlobal("test _ case _ map");
    table.row(0).shouldHave(text("test _ case _ map"));
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:startsActionsMenu")).shouldBe(visible);
    $(By.id("startsForm:projectStarts:0:openProcessViewer")).shouldBe(visible).click();
    $(By.id("startsForm:processViewer:processViewerDialog")).shouldBe(visible);
    $(By.id("viewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("viewerFrame");
    $(By.className("fa-apple")).shouldBe(visible);
    $(By.id("name-id")).shouldHave(value("test _ case _ map"));
  }

  @Test
  void noViewerForUnknownUser() {
    logout();
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:openProcessViewer")).shouldNot(exist);
  }
}
