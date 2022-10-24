package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.customLogin;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.logout;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static com.codeborne.selenide.Condition.readonly;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestHomepageIT {

  @Test
  public void homepageContainers() {
    customLogin("DifferentLogin", "DifferentPassword");

    // start process to create test data
    openView("starts.xhtml");
    $(By.id("startsForm:globalFilter")).setValue("case");
    $(byText("test _ case _ map")).shouldBe(visible).click();
    $(By.id("iFrame")).shouldBe(visible);
    openView("home.xhtml");
    $(".last-starts-card").shouldBe(visible);

    // check if the data is in the containers
    PrimeUi.table(By.id("activeTasks"))
            .contains("Test Developer Workflow-UI Dialog 1");
    PrimeUi.table(By.id("lastStarts"))
            .contains("test _ case _ map");
  }

  @Test
  public void noCardsIfNotLoggedIn() {
    loginDeveloper();

    // cards should be visible when logged in
    $(".active-tasks-card").shouldBe(visible);
    $(".last-starts-card").shouldBe(visible);

    logout();

    // cards should not be visible and starts should appear
    $(".active-tasks-card").shouldNotBe(visible);
    $(".last-starts-card").shouldNotBe(visible);
    $(".main-starts-container").shouldBe(visible);
  }

  @Test
  public void testHomePageViewer() {
    loginDeveloper();

    openView("starts.xhtml");
    $(By.id("startsForm:globalFilter")).setValue("case");
    $(byText("test _ case _ map")).shouldBe(visible).click();
    $(By.id("iFrame")).shouldBe(visible);
    openView("home.xhtml");
    $(".last-starts-card").shouldBe(visible);

    PrimeUi.table(By.id("lastStarts")).contains("test _ case _ map");

    $$(".last-start-element .hidden-text-ellipsis").find(text("test _ case _ map"))
            .parent().findAll("a").last().click();
    $(By.id("processViewer:processViewerDialog")).shouldBe(visible);
    $(By.id("viewerFrame")).shouldBe(visible);
    Selenide.switchTo().frame("viewerFrame");
    $(By.id("name-id")).shouldBe(readonly);
    assertThat($(By.id("name-id")).getAttribute("value")).isEqualTo("test _ case _ map");
    $(By.className("fa-apple")).shouldBe(visible);
  }
}
