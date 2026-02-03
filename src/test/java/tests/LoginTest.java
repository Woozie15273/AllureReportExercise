package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import utilities.DataGenerator;
import utilities.DataGenerator.SignupCredential;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends TestBase {
    private LoginPage login;
    private SignupCredential credential;

    @BeforeMethod
    public void setupPage() {
        credential = new DataGenerator().signup;
        login = new LoginPage(page);
        login.navigation.goToLogin();
    }

    @Test
    public void testRegisterUser() {
        // Test Case 1: Register User
        assertThat(login.signup.signupTitle).isVisible();
        login.signup.signUp(credential);
        assertThat(login.accountInfo.accountInfoTitle).isVisible(); // Verify 'Enter Account Information' is visible
        login.accountInfo.fillInAccountInfo(credential);
        login.addressInfo.fillInAddressInfo(credential);
        login.createAccount();
        assertThat(login.getAccountCreatedPrompt()).isVisible(); // Verify that 'ACCOUNT CREATED!' is visible
        login.clickContinue();
        assertThat(login.getCurrentUser()).isVisible(); // Verify that 'Logged in as username' is visible
        login.deleteAccount();
        assertThat(login.getAccountDeletedPrompt()).isVisible(); // Verify that 'ACCOUNT DELETED!' is visible
        login.clickContinue();
    }

    @Test
    public void testLoginUser() {
        login.setupDummyAccount(credential);

        assertThat(login.login.loginTitle).isVisible(); // Verify 'Login to your account' is visible
        login.login.login(credential.email, credential.password);
        assertThat(login.getCurrentUser()).isVisible(); // Verify 'Login to your account' is visible
        login.deleteAccount();
        assertThat(login.getAccountDeletedPrompt()).isVisible();
    }

    @Test
    public void testLoginIncorrectCredential() {
        // Test Case 3: Login User with incorrect email and password
        assertThat(login.login.loginTitle).isVisible(); // Verify 'Login to your account' is visible
        login.login.login(credential.email, credential.password);
        assertThat(login.getIncorrectLoginPrompt()).isVisible();
    }

    @Test
    public void testLogoutUser() {
        // Test Case 4: Logout User
        login.setupDummyAccount(credential);
        String email = credential.email;
        String password = credential.password;

        assertThat(login.login.loginTitle).isVisible(); // Verify 'Login to your account' is visible
        login.login.login(email, password);
        assertThat(login.getCurrentUser()).isVisible(); // Verify that 'Logged in as username' is visible
        login.logout();
        assertPageToHavePartialURL(".*/login.*");

        login.deleteDummyAccount(email, password);
    }

    @Test
    public void testRegisterDuplicatedEmail() {
        // Test Case 5: Register User with existing email
        login.setupDummyAccount(credential);
        String email = credential.email;
        String password = credential.password;
        String name = credential.fullName;

        assertThat(login.signup.signupTitle).isVisible(); // Verify 'New User Signup!' is visible
        login.signup.signUp(name, email);
        assertThat(login.getDuplicatedEmailPrompt()).isVisible(); // Verify error 'Email Address already exist!' is visible

        login.deleteDummyAccount(email, password); // Cleanup
    }

}
