package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestCaseDetailsIT {

  @BeforeAll
  static void setup() {
    Selenide.closeWebDriver();
    Configuration.proxyEnabled = true;
    Configuration.fileDownload = FileDownloadMode.PROXY;
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
  }

  @BeforeEach
  void beforeEach() {
    open(viewUrl("cases.xhtml"));
    $(byText("Created case of TestData")).shouldBe(visible).click();
    $("#form\\:caseName").shouldBe(text("Created case of TestData"));
  }

  @Test
  public void caseDetails() {
    $("#form\\:creatorUser\\:userName").shouldBe(exactText("DeveloperTest"));
    $("#form\\:caseState").shouldBe(exactText("RUNNING"));

    $("#form\\:caseDestroyBtn").should(visible).click();
    $("#form\\:caseState").shouldBe(exactText("DESTROYED"));
  }

  @Test
  public void taskList() throws Exception {
    Table tasksTable = PrimeUi.table(By.id("form:tasks"));
    tasksTable.valueAt(0, 0).contains("Test Task");
  }

  @Test
  public void checkTaskTableSystemTask() {
    startTestProcess("1750C5211D94569D/testIntermediateEventProcess.ivp");
    open(viewUrl("cases.xhtml"));
    $(".si-information-circle").shouldBe(visible).click();

    Table tasksTable = PrimeUi.table(By.id("form:tasks"));
    tasksTable.containsNot("System");

    $("#form\\:showSystemTasksSwitch").shouldBe(visible).click();

    tasksTable.contains("System");
  }

  @Test
  public void caseList() throws Exception {
    $(".current-hierarchy-case").find("a").shouldHave(text("Created case of TestData"));
  }

  @Test
  public void customFields() throws Exception {
    Table fieldsTable = PrimeUi.table(By.id("form:customFieldsTable"));
    fieldsTable.valueAt(1, 0).contains("field 2");
  }

  @Test
  public void downloadDocument() throws IOException {
    $("#form\\:documentsContainer").shouldHave(text("test.txt"));
    File download = $(".document-entry", 0).find("a").shouldBe(visible).download();
    assertThat(download).hasName("test.txt");
    assertThat(Files.readString(download.toPath())).isEqualTo("this is test document");
  }
}
