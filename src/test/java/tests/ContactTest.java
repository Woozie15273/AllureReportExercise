package tests;

import com.microsoft.playwright.Dialog;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ContactPage;
import utilities.DataGenerator;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Test(groups = {"regression"})
public class ContactTest extends TestBase {

    private ContactPage contact;

    @BeforeMethod
    public void setUpPage(){
        contact = new ContactPage(page);
        contact.navigation.goToContact();
        page.waitForLoadState();
    }

    @Test
    public void testContactUsForm() {
        // Test Case 6: Contact Us Form

        assertThat(contact.form.formTitle).hasText("Get In Touch"); // Verify 'GET IN TOUCH' is visible

        contact.form.completeContactForm(new DataGenerator().contactUs);
        page.onDialog(Dialog::accept); // Register the listener to click OK from Window:confirm()
        contact.form.submitBtn.click();
        assertThat(contact.form.alertSuccess).isVisible(); // Verify success message is visible

        contact.form.btnSuccess.click();
        assertHomePageLoaded(); // Verify that landed to home page successfully
    }
}
