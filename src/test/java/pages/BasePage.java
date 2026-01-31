package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import utilities.DataGenerator;

public class BasePage {
    protected final Page page;
    public final Navigation navigation;
    public final Footer footer;
    public final CartModal cartModal;

    public BasePage(Page page) {
        this.page = page;
        this.navigation = new Navigation();
        this.footer = new Footer();
        this.cartModal = new CartModal();
    }

    public class Navigation {
        public final Locator home;
        public final Locator products;
        public final Locator cart;
        public final Locator login;
        public final Locator testCases;
        public final Locator contact;

        public Navigation() {
            Locator navBar = page.locator(".nav");

            home      = navBar.getByText("Home");
            products  = navBar.getByText("Products");
            cart      = navBar.getByText("Cart");
            login     = navBar.getByText("Login");
            testCases = navBar.getByText("Test Cases");
            contact   = navBar.getByText("Contact");
        }

        public void goToHome() {
            home.click();
        }

        public void goToProducts() {
            products.click();
        }

        public void goToCart() {
            cart.click();
        }

        public void goToLogin() {
            login.click();
        }

        public void goToTestCases() {
            testCases.click();
        }

        public void goToContact() {
            contact.click();
        }

    }

    public class Footer {
        public final Locator subscriptionTitle;
        public final Locator subscriptionInput;
        public final Locator subscriptionBtn;
        public final Locator subscriptionText;

        public Footer() {
            subscriptionTitle = page.getByText("Subscription");
            subscriptionInput = page.locator("#susbscribe_email"); // Original typo on the page
            subscriptionBtn = page.locator("#subscribe");
            subscriptionText = page.getByText("You have been successfully subscribed!");
        }

        public void hitSubscribe() {
            subscriptionInput.fill(new DataGenerator().generateSingleEmail());
            subscriptionBtn.click();
        }

    }

    public class CartModal {
        public final Locator modal;

        public CartModal() {
            modal = page.locator(".modal-content");
        }

        public void viewCart() {
            modal.getByText("View Cart").click();
        }

        public void continueShopping() {
            modal.getByText("Continue Shopping").click();
        }
    }

    public void hitScrollUp() {
        page.locator("#scrollUp").click();
    }

    public void clickOnByText(String text) {
        page.getByText(text).click();
    }

}

