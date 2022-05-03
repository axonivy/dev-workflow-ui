package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
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
    startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
  }

  @Test
  public void downloadDocument() throws IOException {
    loginDeveloper();
    open(viewUrl("cases.xhtml"));
    $(byText("Created case of TestData")).shouldBe(visible).click();
    $("#form\\:caseName").shouldBe(text("Created case of TestData"));
    $("#form\\:documentsContainer").shouldHave(text("test.txt"));
    File download = $(".document-entry", 0).find("a").shouldBe(visible).download();
    assertThat(download).hasName("test.txt");
    assertThat(Files.readString(download.toPath())).isEqualTo("this is test document");
  }

}
