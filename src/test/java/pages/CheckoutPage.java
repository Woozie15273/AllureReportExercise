package pages;

import com.microsoft.playwright.Page;

import java.util.Arrays;

public class CheckoutPage extends BasePage {

    public CheckoutPage(Page page) {
        super(page);
    }

    public String getDeliveryAddress() {
        return getAddressString("#address_delivery");
    }

    public String getBillingAddress() {
        return getAddressString("#address_invoice");
    }

    private String getAddressString(String selector) {
        String s = page.locator(selector).innerText();
        String[] lines = s.split("\\r?\\n");
        return String.join("\n", Arrays.copyOfRange(lines, 1, lines.length)).trim();
    }

    public void addComment(String comment) {
        page.locator(".form-control").fill(comment);
    }

    public void placeOrder() {
        clickOnByText("Place Order");
    }

}
