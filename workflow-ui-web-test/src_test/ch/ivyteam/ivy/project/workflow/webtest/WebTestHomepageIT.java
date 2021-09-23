package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestHomepageIT {

  @Test
  public void homepageContainers() {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();
    Selenide.open(viewUrl("home.xhtml"));

    // start process to create test data
    startTestProcess("1750C5211D94569D/HomePageTestData.ivp");
    Selenide.open(viewUrl("home.xhtml"));

    Table tasksTable = PrimeUi.table(By.id("form:activeTasks"));
    Table startsTable = PrimeUi.table(By.id("form:lastStarts"));

    // check if the data is in the containers
    startsTable.contains("HomePageTestData.ivp");
    tasksTable.contains("Created task of HomePageTestData");
  }

  @Test
  public void noCardsIfNotLoggedIn() {
    loginDeveloper();
    Selenide.open(viewUrl("home.xhtml"));

    // cards should be visible when logged in
    $(By.id("form:activeTasksCard")).shouldBe(visible);
    $(By.id("form:lastStartsCard")).shouldBe(visible);

    logout();

    // cards should not be visible and starts should appear
    $(By.id("form:activeTasksCard")).shouldNotBe(visible);
    $(By.id("form:lastStartsCard")).shouldNotBe(visible);
    $(By.className("main-starts-container")).shouldBe(visible);

  }
}
