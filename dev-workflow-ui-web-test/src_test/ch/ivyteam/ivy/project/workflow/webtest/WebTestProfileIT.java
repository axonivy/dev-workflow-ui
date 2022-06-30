package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.codeborne.selenide.Selenide;

@IvyWebTest
class WebTestProfileIT {

  @Test
  void profile() {
    loginFromTable("testuser");
    openView("profile.xhtml");
    $("#profileForm\\:userName").shouldBe(exactText("testuser"));
    var fullName = $("#profileForm\\:fullName");
    fullName.shouldBe(exactText("testuser"));
    var email = $("#profileForm\\:email");
    email.shouldBe(empty);
    $("#profileForm\\:roles").shouldBe(exactText("Everybody, testrole1, testrole2, testrole3"));

    var contentLanguage = PrimeUi.selectOne(By.id("profileForm:contentLanguage"));
    var formattingLanguage = PrimeUi.selectOne(By.id("profileForm:formattingLanguage"));

    fullName.setValue("fullname for test");
    email.setValue("any@email.com");
    contentLanguage.selectItemByLabel("");
    formattingLanguage.selectItemByLabel("");

    $("#profileForm\\:saveBtn").click();
    Selenide.refresh();

    fullName.shouldBe(exactText("testuser"));
    email.shouldBe(exactText("any@email.com"));
    contentLanguage.selectedItemShould(empty);
    contentLanguage.selectItemByValue("de");

    fullName.setValue("testuser");
    email.setValue("");
    formattingLanguage.selectedItemShould(empty);
    formattingLanguage.selectItemByValue("de_CH");

    $("#profileForm\\:saveBtn").click();
    Selenide.refresh();

    fullName.shouldBe(exactText("testuser"));
    email.shouldBe(empty);
    contentLanguage.selectedItemShould(value("de"));
    formattingLanguage.selectedItemShould(value("de_CH"));
  }
}
