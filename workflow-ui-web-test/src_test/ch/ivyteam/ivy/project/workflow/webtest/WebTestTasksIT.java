package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestTasksIT
{

  @Test
  public void allTasksOnlyAdmin()
  {
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    Selenide.open(viewUrl("home.xhtml"));
    loginFromTable("testuser");
    $(By.id("menuform:sr_allTasks")).shouldNotBe(visible);
    loginDeveloper();
    $(By.id("menuform:sr_allTasks")).shouldBe(visible);
  }

  @Test
  public void testTasksTable() throws Exception
  {
    open(viewUrl("home.xhtml"));
    loginDeveloper();

    startTestProcess("1750C5211D94569D/TestData.ivp");
    open(viewUrl("allTasks.xhtml"));
    Table table = PrimeUi.table(By.id("tasksForm:tasks"));
    table.firstRowContains("TestTask");

    table.valueAt(0, 1).contains("pause");
  }

  @Test
  public void chceckTaskDetails()
  {
    open(viewUrl("home.xhtml"));
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    open(viewUrl("allTasks.xhtml"));

    $(".si-information-circle").shouldBe(visible).click();
    $("#form\\:businessCase").shouldBe(text("TestCase"));

    $("#form\\:taskResponsible").shouldBe(exactText("Everybody"));
    $("#form\\:taskState").shouldBe(exactText("SUSPENDED"));

    $("#form\\:taskActionsBtn").click();
    $("#form\\:taskParkBtn").should(visible).click();

    $("#form\\:taskState").shouldBe(exactText("PARKED"));
    $("#form\\:workingUser").shouldBe(exactText($("#sessionUserName").getText()));

    $("#events\\:0\\:eventType").shouldBe(exactText("EVENT_CREATE_TASK_BY_JOINED_TASKS"));
  }
}
