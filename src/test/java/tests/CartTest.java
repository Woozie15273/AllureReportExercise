package tests;

import com.microsoft.playwright.Locator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import pages.CartPage;
import pages.CartPage.CartInfo.currentEntries;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductsPage;
import pages.ProductsPage.productInfo;
import utilities.DataGenerator;
import utilities.DataGenerator.SignupCredential;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@Test(groups = {"regression", "smoke"})
public class CartTest extends TestBase {

    private HomePage home;
    private ProductsPage products;
    private CartPage cart;
    private LoginPage login;

    @BeforeMethod
    public void setupPages(){
        home = new HomePage(page);
        products = new ProductsPage(page);
        cart = new CartPage(page);
        login = new LoginPage(page);
    }

    @Test
    public void testSubscriptionOnCartPage() {
        // Test Case 11: Verify Subscription in Cart page
        cart.navigation.goToCart();
        cart.footer.subscriptionTitle.scrollIntoViewIfNeeded();
        assertThat(cart.footer.subscriptionTitle).isVisible(); // Verify text 'SUBSCRIPTION' is visible
        cart.footer.hitSubscribe();
        // Verify success message 'You have been successfully subscribed!' is visible
        assertThat(cart.footer.subscriptionText).isVisible();
    }

    @Test
    public void testAddProductsToCart() {
        // Test Case 12: Add Products in Cart
        home.navigation.goToProducts();
        Locator allProducts = products.getAvailableProducts();

        // Add first product
        Locator p1 = allProducts.nth(0);
        productInfo info1 = products.getProductInfoFromWrapper(p1);
        products.addToCartFromHoverOver(p1);
        products.cartModal.continueShopping();

        // Add second product
        Locator p2 = allProducts.nth(1);
        productInfo info2 = products.getProductInfoFromWrapper(p2);
        products.addToCartFromHoverOver(p2);
        products.cartModal.viewCart();

        List<currentEntries> actualCart = cart.cartInfo.getCurrentItemsFromCart();

        // Verify both products are added to Cart
        // Verify their prices, quantity and total price
        assertThat(actualCart)
                .extracting(currentEntries::itemName, currentEntries::price)
                .containsExactlyInAnyOrder(
                        tuple(info1.productName(), info1.price()),
                        tuple(info2.productName(), info2.price())
                );
    }

    @Test
    public void testProductQuantityInCart() {
        // Test Case 13: Verify Product quantity in Cart
        home.navigation.goToProducts();
        Locator p = products.getAvailableProducts().nth(0);
        products.viewProduct(p);

        assertPageToHavePartialURL(".*/product_details/.*"); // Verify product detail is opened
        String currentProduct = products.productDetails.productName.innerText();
        int amount = 4;
        products.productDetails.editQuantity(amount);
        products.productDetails.addToCart.click();
        products.cartModal.viewCart();

        List<currentEntries> actualCart = cart.cartInfo.getCurrentItemsFromCart();

        // Verify that product is displayed in cart page with exact quantity
        assertThat(actualCart)
                .extracting(currentEntries::itemName, currentEntries::quantity)
                .containsExactly(tuple(currentProduct, String.valueOf(amount)));
    }

    @Test
    public void testRemoveProductsFromCart() {
        // Test Case 17: Remove Products From Cart
        home.navigation.goToProducts();

        Locator p = products.getAvailableProducts().nth(0);
        String itemName = products.getProductInfoFromWrapper(p).productName();

        products.addToCartFromHoverOver(p);
        products.cartModal.continueShopping();
        products.navigation.goToCart();

        assertPageToHavePartialURL(".*/view_cart.*"); // Verify that cart page is displayed

        cart.removeProduct(itemName);
        // Verify that product is removed from the cart
        assertThat(page.locator("tr").filter(new Locator.FilterOptions().setHasText(itemName))).isHidden();
    }

    @Test
    public void testCartIsSavedAfterLogin(){
        // Test Case 20: Search Products and Verify Cart After Login
        SignupCredential credential = new DataGenerator().signup;

        home.navigation.goToLogin();
        login.setupDummyAccount(credential);

        home.navigation.goToProducts();
        // Verify user is navigated to ALL PRODUCTS page successfully
        assertPageToHavePartialURL(".*/products.*");

        products.searchRandomProduct();
        Locator p = products.getAvailableProducts().nth(0);
        String itemName = products.getProductInfoFromWrapper(p).productName();
        assertThat(page.getByText("Searched Products")).isVisible(); // Verify 'SEARCHED PRODUCTS' is visible
        assertThat(p).isVisible(); // Verify all the products related to search are visible

        products.addToCartFromHoverOver(p);
        products.cartModal.viewCart();
        // Verify that products are visible in cart
        assertThat(page.locator("tr").filter(new Locator.FilterOptions().setHasText(itemName))).isVisible();

        home.navigation.goToLogin();
        login.login.login(credential.email, credential.password);
        home.navigation.goToCart();
        // Verify that those products are visible in cart after login as well
        assertThat(page.locator("tr").filter(new Locator.FilterOptions().setHasText(itemName))).isVisible();

        login.deleteAccount();
        login.clickContinue();
    }


    @Test
    public void testAddRecommendedToCart() {
        // Test Case 22: Add to cart from Recommended items
        Locator container = home.getRecommendedItemsContainer();
        container.scrollIntoViewIfNeeded();
        assertThat(container.getByText("recommended items")).isVisible(); // Verify 'RECOMMENDED ITEMS' are visible

        container.locator(".carousel-inner").hover(); // Stop the carousel animation
        Locator activeItem = container.locator(".active");

        // Get current visible item names
        List<String> activeItemName =
                activeItem
                        .locator(".productinfo p")
                        .allInnerTexts();

        Locator addToCartBtn = activeItem.locator(".add-to-cart");
        for (int i = 0; i < addToCartBtn.count(); i++) {
            addToCartBtn.nth(i).click();
            cart.cartModal.continueShopping();
        }

        cart.navigation.goToCart();

        // Verify that all selected products are displayed in cart page
        for (String s : activeItemName) {
            Locator specificItem = cart.cartInfo.container
                    .locator(".cart_description a")
                    .filter(new Locator.FilterOptions().setHasText(s));

            assertThat(specificItem).isVisible();
        }
    }

}
