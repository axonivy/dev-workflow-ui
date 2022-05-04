package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.codeborne.selenide.Selenide;

import ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil;

@IvyWebTest
public class WebTestHomepageIT {

  @Test
  public void homepageContainers() {
    WorkflowUiUtil.customLogin("DifferentLogin", "DifferentPassword");
    open(viewUrl("home.xhtml"));

    // start process to create test data
    open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).setValue("case");
    $(byText("test _ case _ map")).shouldBe(visible).click();
    $(By.id("iFrame")).shouldBe(visible);
    open(viewUrl("home.xhtml"));
    $(By.id("form:lastStartsCard")).shouldBe(visible);

    var tasksTable = PrimeUi.table(By.id("form:activeTasks"));
    var startsTable = PrimeUi.table(By.id("form:lastStarts"));

    // check if the data is in the containers
    startsTable.contains("test _ case _ map");
    tasksTable.contains("Test Developer Workflow-UI Dialog 1");
  }

  @Test
  public void noCardsIfNotLoggedIn() {
    loginDeveloper();
    open(viewUrl("home.xhtml"));

    // cards should be visible when logged in
    $(By.id("form:activeTasksCard")).shouldBe(visible);
    $(By.id("form:lastStartsCard")).shouldBe(visible);

    logout();

    // cards should not be visible and starts should appear
    $(By.id("form:activeTasksCard")).shouldNotBe(visible);
    $(By.id("form:lastStartsCard")).shouldNotBe(visible);
    $(By.className("main-starts-container")).shouldBe(visible);
  }

  @Test
  public void testHomePageViewer() {
    loginDeveloper();

    open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).setValue("case");
    $(byText("test _ case _ map")).shouldBe(visible).click();
    $(By.id("iFrame")).shouldBe(visible);
    open(viewUrl("home.xhtml"));
    $(By.id("form:lastStartsCard")).shouldBe(visible);

    var startsTable = PrimeUi.table(By.id("form:lastStarts"));
    startsTable.row(0).getText().contains("test _ case _ map");

    $(By.id("form:lastStarts:0:openProcessViewer")).shouldBe(visible).click();
    $(By.id("form:processViewerDialog")).shouldBe(visible);
    $(By.id("iFrame")).shouldBe(visible);
    Selenide.switchTo().frame("iFrame");
    $(By.className("fa-apple")).shouldBe(visible);
    assertThat($(By.id("name-id")).getAttribute("value")).contains("test _ case _ map");
  }
}
