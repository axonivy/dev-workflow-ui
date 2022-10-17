package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestDownloadCaseDocumentsIT {

  @BeforeAll
  static void setup() {
    Selenide.closeWebDriver();
    Configuration.proxyEnabled = true;
    Configuration.fileDownload = FileDownloadMode.PROXY;
    Configuration.timeout = 10000;
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
  }

  @AfterAll
  static void cleanup() {
    Configuration.proxyEnabled = false;
    Configuration.fileDownload = FileDownloadMode.HTTPGET;
    Selenide.closeWebDriver();
  }

  @Test
  public void downloadDocument() throws IOException {
    openView("cases.xhtml");
    $(byText("Created case of TestData")).shouldBe(visible).click();
    $("#caseName").shouldBe(visible).shouldHave(text("Created case of TestData"));
    $(".documents-card").shouldHave(text("test.txt"));
    var downloadDocumentElement = $(byText("test.txt")).shouldBe(visible);
    File download = downloadDocumentElement.scrollIntoView(false).download();
    assertThat(download).hasName("test.txt");
    assertThat(Files.readString(download.toPath())).isEqualTo("this is test document");
  }

}
