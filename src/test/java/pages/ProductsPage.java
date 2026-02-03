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

    public record productInfo(
            String productName,
            String price
    ) {}

    public productInfo getProductInfoFromWrapper(Locator productWrapper) {
        return new productInfo(
                productWrapper.locator(".productinfo p").innerText(),
                productWrapper.locator(".productinfo h2").innerText()
        );
    }

    public void open() {
        navigation.goToProducts();
    }

    public Locator getAvailableProducts() {
        return page.locator(".product-image-wrapper");
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

    public String addFirstAvailableItem() {
        Locator selectedItem = getAvailableProducts().nth(0);
        String itemName = selectedItem.locator(".productinfo p").innerText().trim();
        addToCartFromHoverOver(selectedItem);
        return itemName;
    }

    public String addRandomProductToCart() {
        Locator l = getAvailableProducts();
        int index = new Random().nextInt(l.count());
        Locator selectedItem = l.nth(index);
        String itemName = selectedItem.locator(".productinfo p").innerText().trim();
        addToCartFromHoverOver(selectedItem);
        return itemName;
    }

    public void addToCartFromHoverOver(Locator productWrapper) {
        productWrapper
                .locator(".productinfo")
                .hover();
        productWrapper
                .locator(".overlay-content")
                .getByText("Add to cart")
                .click();
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
        public final Locator addToCart;
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
            addToCart = infoSection.getByText("Add to cart");
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

        public void editQuantity(int amount) {
            quantity.fill(String.valueOf(amount));
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

        public record detailsFromPage(
                String productName,
                String category,
                String price,
                String availability,
                String condition,
                String brand
        ) {}

        public detailsFromPage getDetailsFromPage() {
            return new detailsFromPage(
                    productName.innerText(),
                    category.innerText(),
                    price.innerText(),
                    availability.innerText().split(": ")[1],
                    condition.innerText().split(": ")[1],
                    brand.innerText().split(": ")[1]
            );
        }

        public void submitReview(ProductReview data) {
            reviewUsername.fill(data.name);
            reviewEmail.fill(data.email);
            reviewTextSection.fill(data.review);
            reviewBtn.click();
        }
    }

}
