package com.lh.core.library;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

import static com.lh.core.page.DEKRAScormTestingPage.answerMcqsIfPresent;
import static com.lh.core.page.DEKRAScormTestingPage.clickAllVisibleVideoButtons;

public class ScormNavigator {

    private final ScormFrameHelper helper;
    private final Page page;

    public ScormNavigator(Page page) {
        this.page = page;
        this.helper = new ScormFrameHelper(page);
    }

    public boolean waitForNextButton(int maxWaitSecs) {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < maxWaitSecs * 1000) {
            try {
                if (helper.getNextButton().isVisible()) {
                    return true;
                }
            } catch (PlaywrightException ignored) {}
            page.waitForTimeout(1000);
        }
        return false;
    }

    public void clickNextWithSpeedup() {
        try {
            helper.getNextButton().click();
            helper.setVideoSpeed(16.0);
            page.waitForTimeout(2000);
        } catch (PlaywrightException e) {
            System.out.println("Click error: " + e.getMessage());
        }
    }

    public void handleOptionalInteractions() {
        clickAllVisibleVideoButtons(helper.getFrame());
        answerMcqsIfPresent(helper.getFrame());
    }
}
