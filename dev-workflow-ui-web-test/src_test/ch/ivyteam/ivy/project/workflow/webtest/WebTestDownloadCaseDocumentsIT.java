package ch.ivyteam.ivy.project.workflow.webtest;

// @IvyWebTest
// Highly flaky and hard to fix
// Commented out to prevent maven warnings

/*
 * public class WebTestDownloadCaseDocumentsIT {
 *
 * @BeforeAll
 * static void setup() {
 * Selenide.closeWebDriver();
 * Configuration.proxyEnabled = true;
 * Configuration.fileDownload = FileDownloadMode.PROXY;
 * Configuration.timeout = 10000;
 * startTestProcess("175461E47A870BF8/makeAdminUser.ivp");
 * loginDeveloper();
 * startTestProcess("1750C5211D94569D/TestData.ivp");
 * }
 *
 * @AfterAll
 * static void cleanup() {
 * Configuration.proxyEnabled = false;
 * Configuration.fileDownload = FileDownloadMode.HTTPGET;
 * Selenide.closeWebDriver();
 * }
 *
 * @Test
 * public void downloadDocument() throws IOException {
 * openView("cases.xhtml");
 * $(byText("Created case of TestData")).shouldBe(visible).click();
 * $("#caseName").shouldBe(visible).shouldHave(text("Created case of TestData"));
 * $(".documents-card").shouldHave(text("test.txt"));
 * var downloadDocumentElement = $(byText("test.txt")).shouldBe(visible);
 * var scrolledDocumentElement = downloadDocumentElement.scrollIntoView("{behavior:\"smooth\",block:\"center\"}");
 * Selenide.sleep(500);
 * File download = scrolledDocumentElement.download();
 * assertThat(download).hasName("test.txt");
 * assertThat(Files.readString(download.toPath())).isEqualTo("this is test document");
 * }
 *
 * }
 */
