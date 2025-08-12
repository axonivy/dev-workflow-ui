package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.codeborne.selenide.Selenide;

@IvyWebTest
class WebTestProfileIT {

  @BeforeEach
  void beforeEach() {
    loginFromTable("testuser");
    openView("profile.xhtml");
  }

  @Test
  void profile() {
    $(By.id("profileForm:userName")).shouldBe(exactText("testuser"));

    $(By.id("profileForm:roles")).shouldBe(exactText("Everybody, testrole1, testrole2, testrole3"));

    var email = $(By.id("profileForm:email"));
    var fullName = $(By.id("profileForm:fullName"));

    email.shouldBe(empty);
    email.sendKeys("any@email.com");
    fullName.shouldBe(exactValue("testuser"));
    fullName.clear();
    fullName.sendKeys("fullname for test");
    save();

    email.shouldBe(exactValue("any@email.com"));
    fullName.shouldBe(exactValue("fullname for test"));

    email.clear();
    fullName.setValue("testuser");
    save();

    email.shouldBe(empty);
    fullName.shouldBe(exactValue("testuser"));
  }

  @Test
  void language() {
    var contentLanguage = PrimeUi.selectOne(By.id("profileForm:contentLanguage"));
    var formattingLanguage = PrimeUi.selectOne(By.id("profileForm:formattingLanguage"));
    var contentLanguageInput = $(By.id("profileForm:contentLanguage")).find(By.className("ui-selectonemenu-label"));
    var formattingLanguageInput = $(By.id("profileForm:formattingLanguage")).find(By.className("ui-selectonemenu-label"));

    contentLanguageInput.setValue("");
    formattingLanguageInput.setValue("");
    save();

    contentLanguage.selectedItemShould(empty);
    formattingLanguage.selectedItemShould(empty);
    contentLanguage.selectItemByValue("de");
    formattingLanguage.selectItemByValue("de_CH");
    save();

    contentLanguage.selectedItemShould(value("de"));
    formattingLanguage.selectedItemShould(value("de_CH"));

    contentLanguageInput.setValue("");
    formattingLanguageInput.setValue("");
    save();

    contentLanguage.selectedItemShould(empty);
    formattingLanguage.selectedItemShould(empty);

    contentLanguage.selectItemByValue("en");
    save();
  }

  private void save() {
    $(By.id("profileForm:saveBtn")).click();
    $(By.id("profileForm:growl_container")).has(text("Profile Saved"));
    Selenide.refresh();
  }
}
