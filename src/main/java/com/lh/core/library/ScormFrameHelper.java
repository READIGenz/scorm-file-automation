package com.lh.core.library;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

public class ScormFrameHelper {

    private final Page page;
    private final FrameLocator frame;

    public ScormFrameHelper(Page page) {
        this.page = page;
        this.frame = page.frameLocator("//iframe[@id='SCO']");
    }

    public FrameLocator getFrame() {
        return frame;
    }

    public Locator getNextButton() {
        return frame.locator("//button[@class='navigation-arrow right highlight']");
    }

    public Locator getVideoElement() {
        return frame.locator("//video[@preload='auto']");
    }

    public void setVideoSpeed(double speed) {
        if (getVideoElement().isVisible()) {
            getVideoElement().evaluate("video => video.playbackRate = " + speed);
        }
    }

    public void openFirstLesson() {
        frame.locator("//i[@class='fa fa-bars']").click();
        frame.locator("(//div[@class='kapitel-item-titel'])[1]").click();
        frame.locator("//div[@class='seiten-nav-container active']//div[1]//div[1]//div[3]").click();
    }

    public boolean isTrainingCompleted() {
        Locator completionMessage = frame.locator("//div[contains(text(),'Sie haben die Unterweisung')]");
        try {
            return completionMessage.isVisible();
        } catch (PlaywrightException e) {
            return false;
        }
    }
}
