package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    public HomePage(Page page) {
        super(page);
    }

    // 3 elements rotating to be visible, get any of them that is currently visible
    public Locator heroText = page
            .getByText("Full-Fledged practice website for Automation Engineers")
            .filter(new Locator.FilterOptions().setVisible(true))
            .first();

    public Locator getRecommendedItemsContainer() {
        return page.locator(".recommended_items");
    }
}
