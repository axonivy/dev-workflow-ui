package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.viewUrl;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;

@IvyWebTest
public class WebTestGLSPProcessViewerIT {

  @Test
  public void testOpenViewer() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).sendKeys("testdata/testdata.ivp");
    $(By.className("si-hierarchy-6")).shouldBe(visible).click();

    $(By.id("iFrame")).shouldBe(visible);
    Selenide.switchTo().frame("iFrame");
    $(By.id("sprotty_1750C5211D94569D-f0")).shouldBe(visible, text("TestData"));
    $$(By.className("start")).shouldBe(sizeGreaterThanOrEqual(1));
    $$(By.className("sprotty-edge")).shouldBe(sizeGreaterThanOrEqual(1));
    $$(By.className("end")).shouldBe(sizeGreaterThanOrEqual(1));
  }

  @Test
  public void testNoViewerForCaseMap() {
    Selenide.open(viewUrl("starts.xhtml"));
    $(By.id("startsForm:filter")).sendKeys("test _ case _ map");
    $(By.className("si-hierarchy-6")).shouldNotBe(visible);
  }
}
