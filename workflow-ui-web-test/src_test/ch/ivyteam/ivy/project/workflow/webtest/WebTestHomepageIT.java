package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestHomepageIT
{

  @Test
  public void homepageContainers()
  {
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
    tasksTable.contains("HomePageTestTask");
  }

  public void noStartsIfNotLoggedIn()
  {
    // start process to get different data
    startTestProcess("1750C5211D94569D/startBoundarySignal.ivp");
    Selenide.open(viewUrl("home.xhtml"));

    Table tasksTable = PrimeUi.table(By.id("form:activeTasks"));
    Table startsTable = PrimeUi.table(By.id("form:lastStarts"));

    // make sure data isnt in the tables
    startsTable.containsNot("startBoundarySignal.ivp");
    tasksTable.containsNot("[Task:");

    // make sure there is no data if user is not logged in
    logout();
    startsTable.contains("No records found");
    tasksTable.contains("No records found");
  }
}
