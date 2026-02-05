package tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static utilities.ConfigReader.getHeadless;
import static utilities.ConfigReader.getWebsite;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;

public class TestBase {

    private final String website = getWebsite();
    private APIRequestContext request;

    /** Page Factory */
    // Shared between all tests
    protected Playwright playwright;
    protected Browser browser;

    // New instance for each test method
    protected BrowserContext context;
    protected Page page;

    /** Driver Factory */
    @BeforeClass(alwaysRun = true)
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(getHeadless())
        );
    }

    @AfterClass(alwaysRun = true)
    void closeBrowser() {
        playwright.close();
    }

    @BeforeMethod(alwaysRun = true)
    void createContextAndPage() {
        context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(1920, 1080));
        page = context.newPage();
        traceViewerManager(context); // Start Trace Viewer

        initiateWebsite();
    }

    private void initiateWebsite() {
        //Shared steps by all test cases: Launch browser, navigate, and verify the homepage is visible
        page.navigate(website);
        assertHomePageLoaded();
    }

    public void assertHomePageLoaded() {
        assertThat(page).hasURL(website);
        assertThat(page.locator("div.logo")).isVisible();
    }

    public void assertPageToHavePartialURL(String regex) {
        assertThat(page).hasURL(Pattern.compile(regex));
    }

    private void traceViewerManager(BrowserContext context){
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true));
    }

    @AfterMethod(alwaysRun = true)
    void closeContext(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            context.tracing().stop(
                    new Tracing.StopOptions().setPath(
                            Paths.get("target/traces/" + System.currentTimeMillis() + "-trace.zip")));
        } else {
            context.tracing().stop();
        }
        context.close();
    }

}
