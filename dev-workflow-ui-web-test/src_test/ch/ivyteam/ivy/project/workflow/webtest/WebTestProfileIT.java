package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginFromTable;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.cssClass;
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
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

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

  @Test
  void notifications() {
    Table table = new Table(By.id("profileForm:notificationChannelsTable"));
    var webNewTaskIcon = $(By.className("subscription-icon"));
    var webNewTaskCheckbox = $(By.id("profileForm:notificationChannelsTable:0:channels:0:subscriptionCheckbox")).lastChild().lastChild();

    table.valueAtShouldBe(0,0, text("New task assigned"));
    table.valueAtShouldBe(1,0, text("Business Information"));
    tableHeader("profileForm:notificationChannelsTable", 0).shouldHave(text("Event"));
    tableHeader("profileForm:notificationChannelsTable", 1).shouldHave(text("Web"));

    shouldHaveSubscribedByDefaultState(webNewTaskIcon, webNewTaskCheckbox);

    webNewTaskCheckbox.parent().click();
    shouldHaveSubscribedState(webNewTaskIcon, webNewTaskCheckbox);

    webNewTaskCheckbox.parent().click();
    shouldHaveNotSubscribedState(webNewTaskIcon, webNewTaskCheckbox);

    // Refresh without saving
    Selenide.refresh();
    shouldHaveSubscribedByDefaultState(webNewTaskIcon, webNewTaskCheckbox);

    // Refresh after saving
    webNewTaskCheckbox.parent().click();
    save();
    shouldHaveSubscribedState(webNewTaskIcon, webNewTaskCheckbox);

    webNewTaskCheckbox.parent().click();
    webNewTaskCheckbox.parent().click();
    save();
    shouldHaveSubscribedByDefaultState(webNewTaskIcon, webNewTaskCheckbox);
  }

  private SelenideElement tableHeader(String table, int column) {
    return $(By.id(table + "_head")).find("tr", 0).find("th", column);
  }

  private void save() {
    $("#profileForm\\:saveBtn").click();
    Selenide.refresh();
  }

  private void shouldHaveSubscribedByDefaultState(SelenideElement webNewTaskIcon,
          SelenideElement webNewTaskCheckbox) {
    iconShouldHaveState(webNewTaskIcon, true, true, "Subscribed by default");
    checkboxShouldHaveState(webNewTaskCheckbox, 0);
  }

  private void shouldHaveSubscribedState(SelenideElement webNewTaskIcon, SelenideElement webNewTaskCheckbox) {
    iconShouldHaveState(webNewTaskIcon, true, false, "Subscribed");
    checkboxShouldHaveState(webNewTaskCheckbox, 1);
  }

  private void shouldHaveNotSubscribedState(SelenideElement webNewTaskIcon,
          SelenideElement webNewTaskCheckbox) {
    iconShouldHaveState(webNewTaskIcon, false, false, "Not subscribed");
    checkboxShouldHaveState(webNewTaskCheckbox, 2);
  }

  private void iconShouldHaveState(SelenideElement webNewTaskIcon, boolean iconSubscribedState,
          boolean iconByDefaultState, String iconTitle) {
    iconShouldHaveSubscribedState(webNewTaskIcon, iconSubscribedState);
    iconShouldHaveByDefaultState(webNewTaskIcon, iconByDefaultState);
    iconShouldHaveTitle(webNewTaskIcon, iconTitle);
  }

  private void checkboxShouldHaveState(SelenideElement checkbox, int state) {
    switch (state) {
      case 0 -> checkbox.shouldNotHave(cssClass("ui-icon"));
      case 1 -> checkbox.shouldHave(cssClass("ui-icon-check"));
      case 2 -> checkbox.shouldHave(cssClass("ui-icon-closethick"));
      default -> throw new IllegalArgumentException("Unexpected value: " + state);
    }
  }

  private void iconShouldHaveSubscribedState(SelenideElement icon, boolean subscribed) {
    if (subscribed) {
      icon.shouldHave(cssClass("si-check-circle-1"));
    } else {
      icon.shouldHave(cssClass("si-remove-circle"));
    }
  }

  private void iconShouldHaveByDefaultState(SelenideElement icon, boolean byDefault) {
    if (byDefault) {
      icon.shouldHave(cssClass("light"));
    } else {
      icon.shouldNotHave(cssClass("light"));
    }
  }

  private void iconShouldHaveTitle(SelenideElement icon, String title) {
    icon.shouldHave(attribute("title", title));
  }
}
