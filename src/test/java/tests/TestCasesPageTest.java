package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.TestCasesPage;

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
        assertPageToHavePartialURL(".*/test_cases.*"); // Verify user is navigated to test cases page successfully
    }


}
