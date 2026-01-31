package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.TestCasesPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestCasesPageTest extends TestBase{

    private TestCasesPage tCases;

    @BeforeMethod
    public void setUpPage(){
        tCases = new TestCasesPage(page);
        tCases.navigation.goToTestCases();
    }

    @Test
    public void testTestCasePageNavigation() {
        // Test Case 7: Verify Test Cases Page
        String expectedUrl = "https://automationexercise.com/test_cases";
        assertThat(page).hasURL(expectedUrl); // Verify user is navigated to test cases page successfully
    }


}
