package pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utilities.DataGenerator.CreditCard;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PaymentPage extends BasePage {

    public PaymentPage(Page page) {
        super(page);
    }

    public void enterPaymentDetail(String cardHolder, CreditCard cc) {
        String[] dateParts = cc.expireDate.split("-");
        String year = dateParts[0];
        String month = dateParts[1];

        page.locator("input[name='name_on_card']").fill(cardHolder);
        page.locator("input[name='card_number']").fill(cc.cardNumber);
        page.locator("input[name='cvc']").fill(cc.cvc);
        page.locator("input[name='expiry_month']").fill(month);
        page.locator("input[name='expiry_year']").fill(year);
    }

    public Locator getOrderSuccessfullyPrompt() {
        /**
         * Cannot solve the browser stop, route, or windows.stop() to intercept the original request.
         * Use the text in the next page instead
         * */
        return page.getByText("Order Placed!");
    }

    public void payAndConfirmOrder() {
        clickOnByText("Pay and Confirm Order");
    }

    public String downloadInvoice() {
        Download dl = page.waitForDownload(() -> {
            clickOnByText("Download Invoice");
        });

        if (dl.failure() != null) {
            return null;
        }

        Path targetPath = Paths.get("target/test-downloads", dl.suggestedFilename());

        dl.saveAs(targetPath);

        if (targetPath.toFile().exists() && targetPath.toFile().length() > 0) {
            return targetPath.toAbsolutePath().toString();
        } else {
            return null;
        }
    }
}
