package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;

@IvyWebTest
class WebTestNotification {

  @BeforeEach
  void beforeEach() {
    loginDeveloper();
  }

  @Test
  void markAsRead() {
    createNotification();

    $(By.id("showNotifications")).should(visible).click();

    $(By.id("notificationForm:notifications:0:notificationMessage"))
        .should(visible)
        .should(text("New Task 'New Invoice for Alban Bislimi' for Everybody"));
    $(By.id("notificationForm:notifications:0:notificationMarkAsRead"))
        .should(visible)
        .click();
    $(By.id("notificationForm:notifications:0:notificationMarkAsRead"))
        .should(not(visible));
  }

  @Test
  void markAllAsRead() {
    createNotification();
    createNotification();

    $(By.id("showNotifications")).should(visible).click();

    $(By.id("notificationForm:notifications:0:notificationMarkAsRead")).should(visible);
    $(By.id("notificationForm:notifications:1:notificationMarkAsRead")).should(visible);

    $(By.id("notificationMarkAllAsRead")).should(visible).click();
    $(By.id("notificationMarkAllAsRead")).should(not(visible));
    $(By.id("notificationForm:notifications:0:notificationMarkAsRead")).should(not(visible));
    $(By.id("notificationForm:notifications:1:notificationMarkAsRead")).should(not(visible));
  }

  @Test
  void hideAll() {
    createNotification();
    createNotification();

    $(By.id("showNotifications")).should(visible).click();

    $(By.id("notificationForm:notifications:0:notificationMarkAsRead")).should(visible);
    $(By.id("notificationForm:notifications:1:notificationMarkAsRead")).should(visible);

    $(By.id("notificationHideAll")).should(visible).click();
    $(By.id("notificationHideAll")).should(not(visible));
    $(By.id("notificationForm:notifications:0:notificationMarkAsRead")).should(not(visible));
    $(By.id("notificationForm:notifications:1:notificationMarkAsRead")).should(not(visible));
    $(By.id("no-notifications")).should(visible);
  }

  @Test
  void info() {
    createNotification();

    $(By.id("showNotifications")).should(visible).click();
    $(By.id("notificationForm:notifications:0:notificationMessage")).should(visible).click();
    $(By.id("taskDetail")).should(visible);
  }

  @Test
  void run() {
    createNotification();

    $(By.id("showNotifications")).should(visible).click();
    $(By.id("notificationForm:notifications:0:notificationAction")).should(visible).click();
    $(By.id("iFrameForm:frameTaskName")).should(visible);
  }

  private void createNotification() {
    startTestProcess("1750C5211D94569D/createNotification.ivp");
  }
}
