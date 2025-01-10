package ch.ivyteam.ivy.project.workflow.webtest.util;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.assertCurrentUrlContains;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.primeui.PrimeUi;

public class Navigation {

  public static void openTasks() {
    openView("tasks.xhtml");
  }

  public static void openTask(String name) {
    openTasks();
    var table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.row(0).shouldHave(text(name));
    $(By.id("tasksForm:tasks:0:taskName")).shouldBe(visible).click();
    assertCurrentUrlContains("/task.xhtml?id=");
    $(By.id("taskName")).shouldBe(visible).shouldHave(text(name));
  }

  public static void openCase(String name) {
    openView("cases.xhtml");
    var table = PrimeUi.table(By.id("casesForm:cases"));
    table.row(0).shouldHave(text(name));
    $(By.id("casesForm:cases:0:caseName")).shouldBe(visible).click();
    assertCurrentUrlContains("/case.xhtml?id=");
    $(By.id("caseName")).shouldBe(visible).shouldHave(text(name));
  }
}
