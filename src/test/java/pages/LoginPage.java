package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import com.microsoft.playwright.options.SelectOption;
import utilities.DataGenerator.SignupCredential;

public class LoginPage extends BasePage {
    public final Login login;
    public final Signup signup;
    public final AccountInfo accountInfo;
    public final AddressInfo addressInfo;

    public LoginPage(Page page) {
        super(page);
        this.login = new Login();
        this.signup = new Signup();
        this.accountInfo = new AccountInfo();
        this.addressInfo = new AddressInfo();
    }

    public class Login {
        public final Locator loginTitle;
        public final Locator loginEmail;
        public final Locator loginPassword;
        public final Locator loginBtn;

        public Login() {
            Locator form = page.locator(".login-form");

            loginTitle = form.getByText("Login to your account");
            loginEmail = form.locator("//input[@name='email']");
            loginPassword = form.locator("//input[@name='password']");
            loginBtn = form.locator("//button[@type='submit']");
        }

        public void login(String email, String pwd) {
            loginEmail.fill(email);
            loginPassword.fill(pwd);
            loginBtn.click();
        }
    }

    public class Signup {
        public final Locator signupTitle;
        public final Locator signupName;
        public final Locator signupEmail;
        public final Locator signupBtn;

        public Signup() {
            Locator form = page.locator(".signup-form");

            signupTitle = form.getByText("New User Signup!");

            signupName = form.locator("//input[@name='name']");
            signupEmail = form.locator("//input[@name='email']");
            signupBtn = form.locator("//button[@type='submit']");

        }

        public void signUp(SignupCredential credential) {
            signupName.fill(credential.fullName);
            signupEmail.fill(credential.email);
            signupBtn.click();
        }

        public void signUp(String name, String email) {
            signupName.fill(name);
            signupEmail.fill(email);
            signupBtn.click();
        }
    }

    public class AccountInfo {
        public final Locator accountInfoTitle;
        public final Locator genderTitle;
        public final Locator nameAccountInfo;
        public final Locator emailAccountInfo;
        public final Locator passwordAccountInfo;
        public final Locator dayDOB;
        public final Locator monthDOB;
        public final Locator yearDOB;
        public final Locator newsletter;
        public final Locator offers;

        public AccountInfo(){
            accountInfoTitle = page.getByText("Enter Account Information");
            genderTitle = page.locator("div.radio");
            nameAccountInfo = page.locator("#name");
            emailAccountInfo = page.locator("#email");
            passwordAccountInfo = page.locator("#password");
            dayDOB = page.locator("#days"); // dropdown
            monthDOB = page.locator("#months"); // dropdown
            yearDOB = page.locator("#years"); // dropdown
            newsletter = page.locator("#newsletter");
            offers = page.locator("#optin");
        }

        public void fillInAccountInfo(SignupCredential signupCredential) {
            chooseRandomTitle();
            nameAccountInfo.fill(signupCredential.fullName);
            passwordAccountInfo.fill(signupCredential.password);
            selectDOB(signupCredential);
            checkNewsletter();
            checkOffers();
        }

        public void chooseRandomTitle() {
            int randomIndex = (int) (Math.random() * genderTitle.count());
            genderTitle.nth(randomIndex).click();
        }

        public void selectDOB(SignupCredential signupCredential) {
            String s = signupCredential.dob; // yyyy-MM-dd
            String[] yyyyMMdd = s.trim().split("-");

            yearDOB.selectOption(yyyyMMdd[0]);
            int monthIndex = Integer.parseInt(yyyyMMdd[1]) - 1;
            monthDOB.selectOption(new SelectOption().setIndex(monthIndex));
            int dayIndex = Integer.parseInt(yyyyMMdd[2]) - 1;
            dayDOB.selectOption(new SelectOption().setIndex(dayIndex));
        }

        public void checkNewsletter() {
            newsletter.click();
        }

        public void checkOffers() {
            offers.click();
        }

    }

    public class AddressInfo {
        public final Locator firstName;
        public final Locator lastName;
        public final Locator company;
        public final Locator address;
        public final Locator address2;
        public final Locator country; // dropdown
        public final Locator state;
        public final Locator city;
        public final Locator zipcode;
        public final Locator mobile;

        public AddressInfo(){
            firstName = page.locator("#first_name");
            lastName = page.locator("#last_name");
            company = page.locator("#company");
            address = page.locator("#address1");
            address2 = page.locator("#address2");
            country = page.locator("#country");
            state = page.locator("#state");
            city = page.locator("#city");
            zipcode = page.locator("#zipcode");
            mobile = page.locator("#mobile_number");

        }

        public void fillInAddressInfo(SignupCredential credential) {
            country.selectOption("United States"); // Hard code due to address localization issue

            firstName.fill(credential.firstName);
            lastName.fill(credential.lastName);
            company.fill(credential.company);
            address.fill(credential.address);
            state.fill(credential.state);
            city.fill(credential.city);
            zipcode.fill(credential.zipcode);
            mobile.fill(credential.mobile);
        }
    }

    public Locator getAccountCreatedPrompt() {
        return page.getByText("Account Created!");
    }

    public Locator getCurrentUser() {
        return page.locator(".fa-user");
    }

    public Locator getAccountDeletedPrompt() {
        return page.getByText("Account Deleted!");
    }

    public Locator getIncorrectLoginPrompt() {
        return page.getByText("Your email or password is incorrect!");
    }

    public Locator getDuplicatedEmailPrompt() {
        return page.getByText("Email Address already exist!");
    }

    public void createAccount() {
        clickOnByText("Create Account");
    }

    public void deleteAccount() {
        clickOnByText("Delete Account");
    }

    public void clickContinue() {
        clickOnByText("Continue");
    }

    public void logout() {
        clickOnByText("Logout");
    }


}
