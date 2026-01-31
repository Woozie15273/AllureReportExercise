package tests;

import com.microsoft.playwright.Locator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;

import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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


    // Test Case 12: Add Products in Cart
    // 4. Click 'Products' button
    // 5. Hover over first product and click 'Add to cart'
    // 6. Click 'Continue Shopping' button
    // 7. Hover over second product and click 'Add to cart'
    // 8. Click 'View Cart' button
    // 9. Verify both products are added to Cart
    // 10. Verify their prices, quantity and total price

    // Test Case 13: Verify Product quantity in Cart
    // 4. Click 'View Product' for any product on home page
    // 5. Verify product detail is opened
    // 6. Increase quantity to 4
    // 7. Click 'Add to cart' button
    // 8. Click 'View Cart' button
    // 9. Verify that product is displayed in cart page with exact quantity

    // Test Case 17: Remove Products From Cart
    // 4. Add products to cart
    // 5. Click 'Cart' button
    // 6. Verify that cart page is displayed
    // 7. Click 'X' button corresponding to particular product
    // 8. Verify that product is removed from the cart

    // Test Case 20: Search Products and Verify Cart After Login
    // 3. Click on 'Products' button
    // 4. Verify user is navigated to ALL PRODUCTS page successfully
    // 5. Enter product name in search input and click search button
    // 6. Verify 'SEARCHED PRODUCTS' is visible
    // 7. Verify all the products related to search are visible
    // 8. Add those products to cart
    // 9. Click 'Cart' button and verify that products are visible in cart
    // 10. Click 'Signup / Login' button and submit login details
    // 11. Again, go to Cart page
    // 12. Verify that those products are visible in cart after login as well

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

        // Get current cart entries
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
