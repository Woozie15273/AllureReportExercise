package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import utilities.DataGenerator.ContactUs;

import java.nio.file.Paths;

import static utilities.ConfigReader.getSampleFile;

public class ContactPage extends BasePage {
    public final Form form;

    public ContactPage(Page page) {
        super(page);
        this.form = new Form();
    }

    public class Form {
        public final Locator formTitle;
        public final Locator nameInput;
        public final Locator emailInput;
        public final Locator subjectInput;
        public final Locator textInput;
        public final Locator fileInput;
        public final Locator submitBtn;

        public final Locator alertSuccess;
        public final Locator btnSuccess;

        public Form(){
            Locator form = page.locator("div.contact-form");

            formTitle = form.locator(".title");
            nameInput = form.locator("[name='name']");
            emailInput = form.locator("[name='email']");
            subjectInput = form.locator("[name='subject']");
            textInput = form.locator("[name='message']");
            fileInput = form.locator("[name='upload_file']");
            submitBtn = form.locator("[name='submit']");

            alertSuccess = form.getByText("Success! Your details have been submitted successfully.");
            btnSuccess = form.getByText("Home");
        }

        public void completeContactForm(ContactUs contact) {
            form.nameInput.fill(contact.name);
            form.emailInput.fill(contact.email);
            form.subjectInput.fill(contact.subject);
            form.textInput.fill(contact.message);

            form.fileInput.setInputFiles(Paths.get(getSampleFile()));
        }
    }

}
