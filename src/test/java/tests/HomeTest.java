package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomeTest extends TestBase {
    private HomePage home;

    @BeforeMethod
    public void setUpPage() {
        home = new HomePage(page);
        helperValidateFooter();
    }

    @Test
    public void testSubscriptionOnHomePage() {
        // Test Case 10: Verify Subscription in home page
        home.footer.hitSubscribe();
        // Verify success message 'You have been successfully subscribed!' is visible
        assertThat(home.footer.subscriptionText).isVisible();
    }

    @Test
    public void testScrollUpBtn(){
        // Test Case 25: Verify Scroll Up using 'Arrow' button and Scroll Down functionality

        // Verify that page is scrolled up by clicking the button
        home.hitScrollUp();
        assertThat(home.heroText).isVisible();
    }

    @Test
    public void testScrollUpManual() {
        // Test Case 26: Verify Scroll Up without 'Arrow' button and Scroll Down functionality

        // Verify that page is scrolled up and 'Full-Fledged practice website for Automation Engineers' text is visible on screen
        home.heroText.scrollIntoViewIfNeeded();
        assertThat(home.heroText).isVisible();
    }

    private void helperValidateFooter() {
        // Common steps shared by all 3 test cases
        home.footer.subscriptionTitle.scrollIntoViewIfNeeded();
        assertThat(home.footer.subscriptionTitle).isVisible(); // Verify text 'SUBSCRIPTION' is visible
    }
}
