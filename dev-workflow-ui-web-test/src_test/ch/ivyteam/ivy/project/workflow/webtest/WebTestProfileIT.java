package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
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
  void userName() {
    $("#profileForm\\:userName").shouldBe(exactText("testuser"));
  }

  @Test
  void roles() {
    $("#profileForm\\:roles").shouldBe(exactText("Everybody, testrole1, testrole2, testrole3"));
  }

  @Test
  void email() {
    var email = $("#profileForm\\:email");
    email.shouldBe(empty);
    email.sendKeys("any@email.com");
    save();

    email.shouldBe(exactValue("any@email.com"));
    email.clear();
    save();

    email.shouldBe(empty);
  }

  @Test
  void fullName() {
    var fullName = $("#profileForm\\:fullName");
    fullName.shouldBe(exactValue("testuser"));
    fullName.clear();
    fullName.sendKeys("fullname for test");
    save();

    fullName.shouldBe(exactValue("fullname for test"));
    fullName.clear();
    fullName.sendKeys("testuser");
    save();

    fullName.shouldBe(exactValue("testuser"));
  }

  @Test
  void contentLanguage() {
    var contentLanguage = PrimeUi.selectOne(By.id("profileForm:contentLanguage"));
    contentLanguage.selectItemByLabel("");
    save();

    contentLanguage.selectedItemShould(empty);
    contentLanguage.selectItemByValue("de");
    save();

    contentLanguage.selectedItemShould(value("de"));
  }

  @Test
  void formattingLanguage() {
    var formattingLanguage = PrimeUi.selectOne(By.id("profileForm:formattingLanguage"));
    formattingLanguage.selectItemByLabel("");
    save();

    formattingLanguage.selectedItemShould(empty);
    formattingLanguage.selectItemByValue("de_CH");
    save();
    formattingLanguage.selectedItemShould(value("de_CH"));
  }

  private void save() {
    $("#profileForm\\:saveBtn").click();
    Selenide.refresh();
  }
}
