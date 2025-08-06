package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.widget.Table;

import ch.ivyteam.ivy.project.workflow.webtest.util.Navigation;

@IvyWebTest
class WebTestProcessViewerPermissionIT {

  @BeforeAll
  static void prepare() {
    startTestProcess("1836AC85C69B7BED/start.ivp");
  }

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
  }

  @AfterAll
  static void finishTask() {
    openView("tasks.xhtml");
    $(byText("Test View Permission Task")).click();
  }

  @Test
  void detail() {
    Navigation.openTask("Test View Permission Task");
    $("a#openProcessViewerBtn").shouldBe(visible);
  }

  @Test
  void caseDetail() {
    Navigation.openCase("Test View Permission Case");
    $(byText("Current Process is hidden")).shouldBe(visible);
  }

  @Test
  void starts() {
    openView("starts.xhtml");
    var table = new Table(By.id("startsForm:projectStarts"));
    $(By.id("startsForm:globalFilter")).setValue("/testdata.ivp");
    table.row(0).shouldHave(text("testdata.ivp"));
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:openProcessViewer")).shouldBe(visible);

    $(By.id("startsForm:globalFilter")).setValue("TestViewPermission");
    table.row(0).shouldHave(text("TestViewPermission"));
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:openProcessViewer")).shouldNotBe(visible);
  }
}
