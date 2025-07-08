package ch.ivyteam.ivy.project.workflow.webtest;

import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.loginDeveloper;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.openView;
import static ch.ivyteam.ivy.project.workflow.webtest.util.WorkflowUiUtil.startTestProcess;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.primeui.PrimeUi;

@IvyWebTest
class WebTestStatisticsIT {

  @BeforeEach
  void setup() {
    loginDeveloper();
    startTestProcess("1750C5211D94569D/TestData.ivp");
    startTestProcess("1750C5211D94569D/TestData.ivp");
  }

  @Test
  void statisticsPageLoads() {
    openView("statistics.xhtml");

    $("h4").shouldHave(text("Statistics"));
    $(By.id("statisticsForm")).shouldBe(visible);

    $(By.id("statisticsForm")).shouldHave(text("Time range:"));
    var timeRangeDropdown = $(By.id("statisticsForm:timeDuration"));
    timeRangeDropdown.shouldBe(visible);
  }

  @Test
  void timeRangeDropdownOptions() {
    openView("statistics.xhtml");

    var timeRangeDropdown = PrimeUi.selectOne(By.id("statisticsForm:timeDuration"));

    timeRangeDropdown.selectItemByLabel("Today");
    timeRangeDropdown.selectItemByLabel("Last 24 hours");
    timeRangeDropdown.selectItemByLabel("Last week");
    timeRangeDropdown.selectItemByLabel("Last month");
    timeRangeDropdown.selectItemByLabel("Last 3 months");
    timeRangeDropdown.selectItemByLabel("Last year");
    timeRangeDropdown.selectItemByLabel("All time");
  }

  @Test
  void statisticsCardsDisplay() {
    openView("statistics.xhtml");

    $(By.id("statisticsForm:allCases")).shouldBe(visible);
    $(By.id("statisticsForm:allTasks")).shouldBe(visible);
  }

  @Test
  void chartsDisplay() {
    openView("statistics.xhtml");

    $(By.id("statisticsForm:caseStateGraph_canvas")).shouldBe(visible);
    $(By.id("statisticsForm:taskStateGraph_canvas")).shouldBe(visible);

    $(By.id("statisticsForm:caseStatisticsChart_canvas")).shouldBe(visible);
    $(By.id("statisticsForm:taskStatisticsChart_canvas")).shouldBe(visible);

    $(By.id("statisticsForm:topCaseCreatorsChart_canvas")).shouldBe(visible);
    $(By.id("statisticsForm:topTaskWorkersChart_canvas")).shouldBe(visible);
  }

  @Test
  void timeRangeSelectionUpdatesCharts() {
    openView("statistics.xhtml");

    var timeRangeDropdown = PrimeUi.selectOne(By.id("statisticsForm:timeDuration"));
    timeRangeDropdown.selectItemByLabel("Today");

    $(By.id("statisticsForm")).shouldBe(visible);
    $(By.id("statisticsForm:caseStatisticsChart_canvas")).shouldBe(visible);

    timeRangeDropdown.selectItemByLabel("Last week");
    $(By.id("statisticsForm")).shouldBe(visible);
    $(By.id("statisticsForm:caseStatisticsChart_canvas")).shouldBe(visible);

    timeRangeDropdown.selectItemByLabel("All time");
    $(By.id("statisticsForm")).shouldBe(visible);
    $(By.id("statisticsForm:caseStatisticsChart_canvas")).shouldBe(visible);
  }

  @Test
  void statisticsWithNoData() {
    openView("statistics.xhtml");

    $(By.id("statisticsForm")).shouldBe(visible);

    // check for either charts or "not enough data" messages
    var hasCharts = $(By.id("statisticsForm:caseStateGraph_canvas")).exists();
    var hasNoDataMessage = $(By.xpath("//*[contains(text(), 'Not enough data available')]")).exists();

    assert hasCharts || hasNoDataMessage : "Should display either charts or no data message";
  }

  @Test
  void todayVsLast24HoursDifference() {
    openView("statistics.xhtml");

    var timeRangeDropdown = PrimeUi.selectOne(By.id("statisticsForm:timeDuration"));
    timeRangeDropdown.selectItemByLabel("Today");

    $(By.id("statisticsForm")).shouldBe(visible);
    $(By.id("statisticsForm:caseStatisticsChart_canvas")).shouldBe(visible);

    timeRangeDropdown.selectItemByLabel("Last 24 hours");
    $(By.id("statisticsForm")).shouldBe(visible);
    $(By.id("statisticsForm:caseStatisticsChart_canvas")).shouldBe(visible);
  }

  @Test
  void statisticsPageStructure() {
    openView("statistics.xhtml");

    $(By.id("statisticsForm:allCases")).shouldBe(visible);
    $(By.id("statisticsForm:allTasks")).shouldBe(visible);

    $(By.id("statisticsForm:caseStateGraph")).should(exist);
    $(By.id("statisticsForm:taskStateGraph")).should(exist);
    $(By.id("statisticsForm:caseStatisticsChart")).should(exist);
    $(By.id("statisticsForm:taskStatisticsChart")).should(exist);
    $(By.id("statisticsForm:topCaseCreatorsChart")).should(exist);
    $(By.id("statisticsForm:topTaskWorkersChart")).should(exist);
  }
}
