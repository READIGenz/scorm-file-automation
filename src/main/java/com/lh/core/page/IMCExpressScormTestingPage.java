package com.lh.core.page;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.LinkedHashSet;
import java.util.Set;

public class IMCExpressScormTestingPage extends BasePage {

    Set<String> externalLinks = new LinkedHashSet<>();

    // === Locator Constants ===
    private static final String SCO_IFRAME = "//iframe[@id='SCO']";
    private static final String PLAY_BUTTON = "//div[@role='button' and contains(@class, 'gIzUim')]";
    private static final String NEXT_BUTTON = "//div[@class='sc-bPCIsI gczySn']//div[5]//*[name()='svg']";
    private static final String COURSE_CARD_START = "//div[@class='sc-bPCIsI gczySn']//div[2]//*[name()='svg']";
    private static final String SCORE_LOCATOR = "//div[@class='sc-iPHsxv cRxHqc']";
    private static final String AI_RISKS_HEADER = "//h1[normalize-space()='KI-Risiken']";
    private static final String ALL_DONE_HEADER = "//h1[normalize-space()='All done!']";
    private static final String CARD_UI_BASE = "(//div[@class='sc-eABdvX klSHmA'])";

    Locator playButton = page.frameLocator(SCO_IFRAME).locator(PLAY_BUTTON);
    Locator nextButton = page.frameLocator(SCO_IFRAME).locator(NEXT_BUTTON);

    public void verifyNoExternalResources() throws InterruptedException {
        page.waitForLoadState();
        page.waitForTimeout(5000);
        System.out.println("🧪 Verifying SCORM content for external resources...");

        FrameLocator frameLocator = page.frameLocator(SCO_IFRAME);

        checkElements(frameLocator, "a[href]", "href", "Hyperlink");
        checkElements(frameLocator, "iframe[src]", "src", "iFrame");
        checkElements(frameLocator, "embed[src]", "src", "Embedded Media");
        checkElements(frameLocator, "script[src]", "src", "External Script");
        checkElements(frameLocator, "link[href]", "href", "Stylesheet or Resource Link");

        System.out.println("✅ Finished checking for external resources.");
        if (!externalLinks.isEmpty()) {
            System.out.println("\n❗ External Resources Detected:");
            for (String url : externalLinks) {
                System.out.println("🔗 " + url);
            }
        } else {
            System.out.println("✅ No external resources found.");
        }
    }

    private void checkElements(FrameLocator frameLocator, String selector, String attribute, String elementType) throws InterruptedException {
        Locator elements = frameLocator.locator(selector);
        int count = elements.count();
        System.out.println("🔍 Checking " + count + " " + elementType + "(s) using selector: " + selector);

        for (int i = 0; i < count; i++) {
            String value = elements.nth(i).getAttribute(attribute);
            if (value == null || value.isEmpty()) {
                logger.info("ℹ️  " + elementType + " #" + (i + 1) + " has no " + attribute + " attribute.");
                continue;
            }

            System.out.println("🔗 Found " + elementType + " #" + (i + 1) + ": " + value);

            if (isExternalLink(value)) {
                logger.error("❌ External " + elementType + " detected: " + value);
                externalLinks.add(value);
            } else {
                logger.info("✅ " + elementType + " is internal or safe: " + value);
            }
        }
    }

    private boolean isExternalLink(String url) {
        if (url == null || url.isEmpty()) return false;
        if (url.startsWith("mailto:") || url.startsWith("tel:")) return false;
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public void userStartsIMCExpressCourse() {
        page.waitForLoadState();
        playButton.click(new Locator.ClickOptions().setForce(true));
    }

    public void courseNavigation() throws InterruptedException {
        page.waitForLoadState();

        page.frameLocator(SCO_IFRAME)
                .locator(COURSE_CARD_START).click();

        page.waitForTimeout(6000);

        Locator scoreLocator = page.frameLocator(SCO_IFRAME).locator(SCORE_LOCATOR);
        scoreLocator.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        String scoreText = scoreLocator.textContent();
        System.out.println("✅ Extracted Score: " + scoreText);

        page.frameLocator(SCO_IFRAME)
                .locator(AI_RISKS_HEADER).click();

        int maxClicks = 100;
        int count = 0;

        Locator nextButton = page.frameLocator(SCO_IFRAME).locator(NEXT_BUTTON);
        Locator allDoneLocator = page.frameLocator(SCO_IFRAME).locator(ALL_DONE_HEADER);

        while (!allDoneLocator.isVisible() && count < maxClicks) {
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.waitForTimeout(1000);

            verifyNoExternalResources();

            clickCardUI();

            try {
                nextButton.waitFor(new Locator.WaitForOptions()
                        .setTimeout(5000)
                        .setState(WaitForSelectorState.VISIBLE));
                nextButton.click(new Locator.ClickOptions().setForce(true));
            } catch (Exception e) {
                System.out.println("⚠️ Next button not visible after waiting, stopping navigation.");
                break;
            }

            count++;
        }

        if (allDoneLocator.isVisible()) {
            System.out.println("✅ 'All done!' page reached.");
        } else {
            System.out.println("⚠️ Reached max clicks or error before reaching 'All done!'");
        }

        scormTesting();  // Generate report
    }

    public void clickCardUI() {
        page.waitForTimeout(2000);
        int index = 1;

        while (true) {
            Locator element = page.frameLocator(SCO_IFRAME)
                    .locator(CARD_UI_BASE + "[" + index + "]");

            try {
                if (element.count() == 0 || !element.isVisible(new Locator.IsVisibleOptions().setTimeout(1000))) {
                    break;
                }

                element.click(new Locator.ClickOptions()
                        .setTimeout(3000)
                        .setForce(true));

                index++;
                page.waitForTimeout(2000);
            } catch (Exception e) {
                System.out.println("Error clicking element at index " + index + ": " + e.getMessage());
                break;
            }
        }
    }
}