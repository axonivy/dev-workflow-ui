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
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;

@IvyWebTest
public class WebTestProcessViewerPermissionIT {

  @BeforeAll
  public static void prepare() {
    startTestProcess("1836AC85C69B7BED/start.ivp");
  }

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
  }

  @AfterAll
  public static void finishTask() {
    openView("tasks.xhtml");
    $(byText("Test View Permission Task")).click();
  }

  @Test
  public void taskDetail() {
    openView("tasks.xhtml");
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text("Test View Permission Task"));
    $(By.id("tasksForm:tasks:0:taskName")).shouldBe(visible).click();
    $(By.id("taskName")).shouldHave(text("Test View Permission Task"));
    $("a#openProcessViewerBtn").shouldBe(visible);
  }

  @Test
  public void caseDetail() {
    openView("cases.xhtml");
    $(By.id("casesForm:cases:0:caseName")).shouldBe(visible).click();
    $(By.id("caseName")).shouldBe(visible).shouldHave(text("Test View Permission Case"));
    $(byText("Current Process is hidden")).shouldBe(visible);
  }

  @Test
  public void starts() {
    openView("starts.xhtml");
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("/testdata.ivp");
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:openProcessViewer")).shouldBe(visible);
    $(By.id("startsForm:projectStarts:globalFilter")).setValue("TestViewPermission");
    $(By.id("startsForm:projectStarts:0:startActionsBtn")).shouldBe(visible).click();
    $(By.id("startsForm:projectStarts:0:openProcessViewer")).shouldNotBe(visible);
  }
}
