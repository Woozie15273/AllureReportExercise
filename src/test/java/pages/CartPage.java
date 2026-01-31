package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CartPage extends BasePage {
    public final CartInfo cartInfo;

    public CartPage(Page page) {
        super(page);
        this.cartInfo = new CartInfo();
    }

    public void toCheckout() {
        page.getByText("Proceed To Checkout").click();
    }

    public class CartInfo {
        public final Locator container;

        public CartInfo() {
            container = page.locator("#cart_info");
        }
    }

}
