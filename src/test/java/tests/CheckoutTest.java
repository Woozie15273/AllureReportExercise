package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;
import utilities.DataGenerator;
import utilities.DataGenerator.SignupCredential;
import utilities.DataGenerator.CreditCard;

import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = {"regression", "smoke"})
public class CheckoutTest extends TestBase {

    private HomePage home;
    private ProductsPage products;
    private CartPage cart;
    private LoginPage login;
    private CheckoutPage checkout;
    private PaymentPage payment;

    private SignupCredential credential;
    private CreditCard creditCard;

    private String selectedItem;

    @BeforeMethod
    public void setupPages() {
        credential = new DataGenerator().signup;
        creditCard = new DataGenerator().creditCard;

        home = new HomePage(page);
        products = new ProductsPage(page);
        cart = new CartPage(page);
        login = new LoginPage(page);
        checkout = new CheckoutPage(page);
        payment = new PaymentPage(page);
    }

    @Test
    public void testRegisterWhileCheckout() {
        // Test Case 14: Place Order: Register while Checkout
        helperProductToCheckout();
        checkout.cartModal.modal.waitFor();
        cart.checkoutModal.toRegister();
        helperSignupAccount();

        home.navigation.goToCart();
        cart.toCheckout();

        helperConfirmAddress();

        checkout.addComment(credential.fullAddress);
        checkout.placeOrder();

        helperPayAndConfirmOrder();

        helperDeleteAccount();

    }

    @Test
    public void RegisterBeforeCheckout() {
        // Test Case 15: Place Order: Register before Checkout
        home.navigation.goToLogin();
        helperSignupAccount();
        helperProductToCheckout();
        checkout.addComment(credential.fullAddress);
        checkout.placeOrder();
        helperPayAndConfirmOrder();
        helperDeleteAccount();

    }

    @Test
    public void testLoginBeforeCheckout() {
        // Test Case 16: Place Order: Login before Checkout
        home.navigation.goToLogin();
        helperSignupAccount();
        login.logout();
        login.login.login(credential.email, credential.password);
        helperProductToCheckout();
        checkout.addComment(credential.fullAddress);
        checkout.placeOrder();
        helperPayAndConfirmOrder();
        helperDeleteAccount();
    }

    @Test
    public void verifyAddressDetailsDuringCheckout() {
        // Test Case 23: Verify address details in checkout page
        home.navigation.goToLogin();
        helperSignupAccount();
        helperProductToCheckout();
        helperConfirmAddress();
        helperDeleteAccount();

    }

    @Test
    public void testDownloadInvoice() {
        // Test Case 24: Download Invoice after purchase order
        helperProductToCheckout();

        checkout.cartModal.modal.waitFor();
        cart.checkoutModal.toRegister();
        helperSignupAccount();

        home.navigation.goToCart();
        cart.toCheckout();

        helperConfirmAddress();

        checkout.addComment(credential.fullAddress);
        checkout.placeOrder();

        helperPayAndConfirmOrder();

        assertThat(payment.downloadInvoice()).isNotNull();
        payment.clickOnByText("Continue");

        helperDeleteAccount();

    }

    private void helperSignupAccount() {
        login.signup.signUp(credential);
        login.accountInfo.fillInAccountInfo(credential);
        login.addressInfo.fillInAddressInfo(credential);
        login.createAccount();
        assertThat(login.getAccountCreatedPrompt()).isVisible(); // Verify 'ACCOUNT CREATED!'
        login.clickContinue();
        assertThat(login.getCurrentUser()).isVisible(); // Verify ' Logged in as username' at top
    }

    private void helperProductToCheckout() {
        home.navigation.goToProducts();
        selectedItem = products.addFirstAvailableItem();
        products.cartModal.continueShopping();
        products.navigation.goToCart();
        assertPageToHavePartialURL(".*/view_cart.*");
        cart.toCheckout();
    }

    private void helperConfirmAddress() {
        String delivery = checkout.getDeliveryAddress();
        String billing = checkout.getBillingAddress();

        // Verify Address Details and Review Your Order
        page.getByText(selectedItem).isVisible();
        assertAddressSameAsRegistration(delivery, billing);
    }

    private void helperPayAndConfirmOrder() {
        payment.enterPaymentDetail(credential.fullName, creditCard);
        payment.payAndConfirmOrder();
        assertThat(payment.getOrderSuccessfullyPrompt()).isVisible(); // Verify success message
    }

    private void helperDeleteAccount() {
        login.deleteAccount();
        assertThat(login.getAccountDeletedPrompt()).isVisible(); // Verify that 'ACCOUNT DELETED!' is visible
        login.clickContinue();
    }

    private String wash(String text) {
        if (text == null) return "";
        return text.replaceAll("[\\u00A0\\s]+", " ") // Replaces non-breaking spaces & multi-spaces with 1 space
                .trim();
    }

    private void assertAddressSameAsRegistration(String delivery, String billing) {
        // 1. Identify the unique address blocks to check
        Stream.of(delivery, billing)
                .distinct() // If delivery == billing, only process once
                .map(this::wash) // Clean the actual text from the page
                .forEach(cleanedAddress -> {
                    // 2. Stream through the credentials and assert each one
                    Stream.of(credential.fullName, credential.address, credential.city,
                                    credential.state, credential.zipcode)
                            .map(this::wash) // Clean the expected data
                            .forEach(field ->
                                    assertThat(cleanedAddress)
                                            .as("Address block missing expected field: " + field)
                                            .contains(field)
                            );
                });
    }
}
