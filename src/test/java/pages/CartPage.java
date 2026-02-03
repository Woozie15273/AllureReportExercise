package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {
    public final CartInfo cartInfo;
    public final CheckoutModal checkoutModal;

    public CartPage(Page page) {
        super(page);
        this.cartInfo = new CartInfo();
        this.checkoutModal = new CheckoutModal();
    }

    public void removeProduct(String itemName) {
        Locator container = page.locator("tr").filter(new Locator.FilterOptions().setHasText(itemName));
        container.locator(".cart_quantity_delete").click();
    }

    public void toCheckout() {
        clickOnByText("Proceed To Checkout");
    }

    public class CartInfo {
        public final Locator container;
        public final Locator allCartItems;

        public CartInfo() {
            container = page.locator("#cart_info");
            allCartItems = container.locator("tbody tr");
        }

        public record currentEntries (
            String itemName,
            String category,
            String price,
            String quantity,
            String total
        ) {}

        public List<currentEntries> getCurrentItemsFromCart() {
            List<currentEntries> items = new ArrayList<>();

            String itemNameSelector = ".cart_description a";
            String categorySelector = ".cart_description p";
            String priceSelector = ".cart_price";
            String quantitySelector = ".cart_quantity";
            String totalSelector = ".cart_total";

            List<Locator> rows = allCartItems.all();

            for (Locator row : rows) {
                items.add(new currentEntries(
                        row.locator(itemNameSelector).innerText().trim(),
                        row.locator(categorySelector).innerText().trim(),
                        row.locator(priceSelector).innerText().trim(),
                        row.locator(quantitySelector).innerText().trim(),
                        row.locator(totalSelector).innerText().trim()
                ));
            }

            return items;
        }
    }

    public class CheckoutModal {
        public final Locator modal;

        public CheckoutModal() {
            modal = page.locator(".modal-content");
        }

        public void toRegister() {
            modal.locator("a[href='/login']").click();
        }

        public void continueOnCart() {
            modal.getByText("Continue On Cart").click();
        }
    }

}
