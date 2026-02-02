package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import utilities.DataGenerator;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends TestBase {
    private LoginPage login;
    private DataGenerator data;

    @BeforeMethod
    public void setupPage() {
        data = new DataGenerator();
        login = new LoginPage(page);
        login.navigation.goToLogin();
    }

    @Test
    public void testRegisterUser() {
        // Test Case 1: Register User
        assertThat(login.signup.signupTitle).isVisible();
        login.signup.signUp(data.signup);
        assertThat(login.accountInfo.accountInfoTitle).isVisible(); // Verify 'Enter Account Information' is visible
        login.accountInfo.fillInAccountInfo(data.signup);
        login.addressInfo.fillInAddressInfo(data.signup);
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
        String[] user = login.setupDummyAccount();
        String email = user[0];
        String password = user[1];

        assertThat(login.login.loginTitle).isVisible(); // Verify 'Login to your account' is visible
        login.login.login(email, password);
        assertThat(login.getCurrentUser()).isVisible(); // Verify 'Login to your account' is visible
        login.deleteAccount();
        assertThat(login.getAccountDeletedPrompt()).isVisible();
    }

    @Test
    public void testLoginIncorrectCredential() {
        // Test Case 3: Login User with incorrect email and password
        String email = data.signup.email;
        String pwd = data.signup.password;
        assertThat(login.login.loginTitle).isVisible(); // Verify 'Login to your account' is visible
        login.login.login(email, pwd);
        assertThat(login.getIncorrectLoginPrompt()).isVisible();
    }

    @Test
    public void testLogoutUser() {
        // Test Case 4: Logout User
        String[] user = login.setupDummyAccount();
        String email = user[0];
        String password = user[1];

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
        String[] user = login.setupDummyAccount();
        String email = user[0];
        String password = user[1];
        String name = user[2];

        assertThat(login.signup.signupTitle).isVisible(); // Verify 'New User Signup!' is visible
        login.signup.signUp(name, email);
        assertThat(login.getDuplicatedEmailPrompt()).isVisible(); // Verify error 'Email Address already exist!' is visible

        login.deleteDummyAccount(email, password); // Cleanup
    }

}
