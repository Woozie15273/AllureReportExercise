package tests;

import com.microsoft.playwright.Locator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ProductsPage;
import utilities.DataGenerator;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductsTest extends TestBase{
    private ProductsPage product;

    @BeforeMethod
    public void setupPage() {
        product = new ProductsPage(page);
        product.open();
        // Verify user is navigated to ALL PRODUCTS page successfully
        assertThat(page).hasURL(Pattern.compile(".*/products.*"));
    }

    @Test
    public void testProductDetailsPage() {
        // Test Case 8: Verify All Products and product detail page
        Locator e = product.getAvailableProducts().getFirst();
        assertThat(e).isVisible(); // Verify if The products list is visible
        product.viewProduct(e);
        // Verify that detail is visible: product name, category, price, availability, condition, brand
        for (Locator l : product.productDetails.getProductDetails()) {
            assertThat(l).isVisible();
        }
    }

    @Test
    public void testSearchProduct() {
        // Test Case 9: Search Product
        product.searchRandomProduct();
        assertThat(page.getByText("Searched Products")).isVisible(); // Verify 'SEARCHED PRODUCTS' is visible
        assertThat(product.getAvailableProducts().getFirst()).isVisible(); // Verify all the products related to search are visible
    }

    @Test
    public void testViewCategories() {
        // Test Case 18: View Category Products
        Locator categoryPanels = product.categoryProducts.categoryContainer.locator(".panel");

        for (int i = 0; i < categoryPanels.count(); i++) {
            Locator panel = categoryPanels.nth(i);
            Locator categoryLink = panel.locator(".panel-heading a");
            String catName = categoryLink.innerText().trim();

            categoryLink.click(); //Expand the category

            // Capture all sub-category links in the CURRENT panel only
            // This prevents the loop from trying to click sub-categories in other sections
            Locator subCategories = panel.locator(".panel-body li a");
            int subCount = subCategories.count();

            for (int j = 0; j < subCount; j++) {
                // Re-locate the sub-category by index inside the current panel
                Locator subCat = subCategories.nth(j);
                String subName = subCat.innerText().trim();

                subCat.click();

                // Assert the header update
                String expectedPattern = String.format(".*%s - %s Products.*", catName, subName);
                assertThat(product.getFeaturesItemsTitle())
                        .hasText(Pattern.compile(expectedPattern, Pattern.CASE_INSENSITIVE));

                categoryLink.click(); // Re-expand the parent category to see the sub-links again.
            }
        }
    }

    @Test
    public void testViewBrands() {
        // Test Case 19: View & Cart Brand Products
        // Iterate all brands and validate their navigation
        Locator brands = product.brandProducts.brandsContainer.locator(".brands-name li");

        List<String> names = brands.allInnerTexts().stream()
                .map(s -> s.replaceAll("\\(\\d+\\)", "").trim())
                .toList();

        for (int i = 0; i < names.size(); i++) {
            brands.nth(i).click();

            // Verification
            assertThat(product.getFeaturesItemsTitle())
                    .hasText(Pattern.compile(".*" + names.get(i) + ".*", Pattern.CASE_INSENSITIVE));
        }
    }

    @Test
    public void testAddReview() {
        // Test Case 21: Add review on product
        DataGenerator data = new DataGenerator();

        Locator e = product.getAvailableProducts().getFirst();
        product.viewProduct(e);
        assertThat(product.productDetails.reviewTitle).isVisible(); // Verify 'Write Your Review' is visible
        product.productDetails.submitReview(data.review);
        assertThat(product.productDetails.reviewSucceedPrompt).isVisible(); // Verify success message 'Thank you for your review.'
    }

}
