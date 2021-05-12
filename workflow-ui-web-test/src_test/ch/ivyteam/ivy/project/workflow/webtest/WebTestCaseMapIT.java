package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestCaseMap;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestCaseMapIT
{

  @BeforeAll
  static void setup()
  {
    Selenide.closeWebDriver();
    Configuration.proxyEnabled = true;
    Configuration.fileDownload = FileDownloadMode.PROXY;
    loginDeveloper();
    startTestCaseMap("0cf1f054-a4ad-4b2b-bcf1-c9c34ec0a2ab.icm");
  }

  @Test
  public void caseDetails()
  {
    open(viewUrl("cases.xhtml"));
    $(".si-information-circle").shouldBe(visible).click();

    $("#form\\:creatorUser\\:userName").shouldBe(exactText("DeveloperTest"));
    $("#form\\:caseState").shouldBe(exactText("CREATED"));

    $(".case-map-column").shouldHave(text("stage1"));
    $(".process-flow").shouldBe(visible);
  }
}
