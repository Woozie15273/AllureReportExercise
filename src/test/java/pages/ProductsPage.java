package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utilities.DataGenerator.ProductReview;

import java.util.*;

public class ProductsPage extends BasePage {
    public final CategoryProducts categoryProducts;
    public final BrandProducts brandProducts;
    public final ProductDetails productDetails;

    public ProductsPage(Page page) {
        super(page);
        this.categoryProducts = new CategoryProducts();
        this.brandProducts = new BrandProducts();
        this.productDetails = new ProductDetails();
    }

    public void open() {
        navigation.goToProducts();
    }

    public List<Locator> getAvailableProducts() {
        return page.locator(".product-image-wrapper").all();
    }

    public Locator getFeaturesItemsTitle() {
        return page.locator(".features_items .title");
    }

    public void viewProduct(Locator productWrapper) {
        productWrapper.getByText("View Product").click();
    }

    public void searchProduct(String productName) {
        page.locator("#search_product").fill(productName);
        page.locator("#submit_search").click();
    }

    public void searchRandomProduct() {
        Locator l = page.locator(".productinfo p");
        int index = new Random().nextInt(l.count());
        searchProduct(l.nth(index).innerText().trim());
    }

    public void addToCart(Locator productWrapper) {
        productWrapper.getByText("Add to cart").click();
    }

    public class CategoryProducts {
        public final Locator categoryContainer;
        public final Locator categoryTitle;

        public CategoryProducts() {
            categoryContainer = page.locator(".category-products");
            categoryTitle = page.locator(".title");
        }
    }

    public class BrandProducts {
        public final Locator brandsContainer;

        public BrandProducts() {
            brandsContainer = page.locator(".brands_products");
        }
    }

    public class ProductDetails {
        public final Locator infoSection;
        public final Locator productName;
        public final Locator category;
        public final Locator price;
        public final Locator quantity;
        public final Locator availability;
        public final Locator condition;
        public final Locator brand;

        public final Locator reviewSection;
        public final Locator reviewTitle;
        public final Locator reviewUsername;
        public final Locator reviewEmail;
        public final Locator reviewTextSection;
        public final Locator reviewBtn;
        public final Locator reviewSucceedPrompt;

        public ProductDetails() {
            infoSection = page.locator(".product-information");
            productName = infoSection.locator("h2");
            category = infoSection.getByText("Category:");
            price = infoSection.getByText("Rs.");
            quantity = infoSection.locator("#quantity");
            availability = infoSection.getByText("Availability:").locator("..");
            condition = infoSection.getByText("Condition:").locator("..");
            brand = infoSection.getByText("Brand:").locator("..");

            reviewSection = page.locator(".shop-details-tab");
            reviewTitle = reviewSection.getByText("Write Your Review");
            reviewUsername = reviewSection.locator("#name");
            reviewEmail = reviewSection.locator("#email");
            reviewTextSection = reviewSection.locator("#review");
            reviewBtn = reviewSection.getByText("Submit");
            reviewSucceedPrompt = reviewSection.getByText("Thank you for your review.");
        }

        public List<Locator> getProductDetails() {
            return Arrays.asList(
                    productName,
                    category,
                    price,
                    availability,
                    condition,
                    brand
            );
        }

        public Map<String, String> getProductDetailsToMap() {
            Map<String, String> details = new LinkedHashMap<>();

            details.put("Product Name", productName.innerText());
            details.put("Category", category.innerText());
            details.put("Price", price.innerText());
            details.put("Availability", availability.innerText().split(": ")[1]);
            details.put("Condition", condition.innerText().split(": ")[1]);
            details.put("Brand", brand.innerText().split(": ")[1]);

            return details;
        }

        public void submitReview(ProductReview data) {
            reviewUsername.fill(data.name);
            reviewEmail.fill(data.email);
            reviewTextSection.fill(data.review);
            reviewBtn.click();
        }
    }

}
