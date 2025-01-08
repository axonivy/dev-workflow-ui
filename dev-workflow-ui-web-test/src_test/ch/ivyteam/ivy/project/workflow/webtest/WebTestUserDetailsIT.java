package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;

@IvyWebTest
class WebTestUserDetailsIT {

  @BeforeAll
  static void setup() {
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
  }

  @Test
  void goToUserDetailPage() {
    loginDeveloper();
    openView("cases.xhtml");
    $(byText("Created case of TestData")).shouldBe(visible).click();
    $(By.id("caseName")).shouldBe(visible).shouldHave(text("Created case of TestData"));

    $(By.id("creatorUser:userName")).shouldNotBe(visible);
    $(By.id("creatorUser:userNameLink")).shouldBe(visible);
    $(By.id("creatorUser:userNameLink")).click();

    $(By.id("userDetailInformationForm")).shouldBe(visible);
    $(By.id("userDetailRoleInformationForm")).shouldBe(visible);
    $(By.id("userDetailInformationForm:fullName")).shouldHave(text("DeveloperTest"));
    $(By.id("userDetailTasksForm")).shouldBe(visible);
    $(By.id("userDetailCasesForm")).shouldBe(visible);
  }

  @Test
  void noCredentialsForUserDetailPage() {
    loginFromTable("testuser");
    $("#sessionUserName").shouldBe(exactText("testuser"));

    openView("cases.xhtml");
    $(byText("Created case of TestData")).shouldBe(visible).click();
    $(By.id("caseName")).shouldBe(visible).shouldHave(text("Created case of TestData"));

    $(By.id("creatorUser:userNameLink")).shouldNotBe(visible);
    $(By.id("creatorUser:userName")).shouldBe(visible);
  }
}
