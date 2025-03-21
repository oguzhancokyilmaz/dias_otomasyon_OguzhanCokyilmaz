package com.ecommerce.automation.pages;

import com.ecommerce.automation.utils.Driver;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Log4j2
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage() {
        this.driver = Driver.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Navigates to a specific URL
     * @param url the URL to navigate to
     */
    public void navigateTo(String url) {
        try {
            driver.get(url);
            log.info("Navigated to URL: {}", url);
        } catch (Exception e) {
            log.error("Failed to navigate to URL: {}", url, e);
            throw e;
        }
    }
    
    /**
     * Waits for an element to be visible
     * @param element the WebElement to wait for
     * @return the WebElement once it's visible
     */
    protected WebElement waitForVisibility(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            log.error("Element is not visible: {}", element, e);
            throw e;
        }
    }
    
    /**
     * Waits for an element to be clickable
     * @param element the WebElement to wait for
     * @return the WebElement once it's clickable
     */
    protected WebElement waitForClickability(WebElement element) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            log.error("Element is not clickable: {}", element, e);
            throw e;
        }
    }
    
    /**
     * Waits for an element to be present in the DOM
     * @param locator the By locator to find the element
     * @return the WebElement once it's present
     */
    protected WebElement waitForPresence(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            log.error("Element is not present: {}", locator, e);
            throw e;
        }
    }
    
    /**
     * Waits for all elements matching the locator to be visible
     * @param locator the By locator to find elements
     * @return the List of WebElements once they're visible
     */
    protected List<WebElement> waitForAllVisibleElements(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (Exception e) {
            log.error("Elements are not visible: {}", locator, e);
            throw e;
        }
    }
    
    /**
     * Checks if an element is displayed
     * @param element the WebElement to check
     * @return true if the element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
    
    /**
     * Clicks on an element with explicit wait
     * @param element the WebElement to click
     */
    protected void click(WebElement element) {
        try {
            waitForClickability(element).click();
            log.debug("Clicked on element: {}", element);
        } catch (Exception e) {
            log.error("Failed to click on element: {}", element, e);
            throw e;
        }
    }
    
    /**
     * Types text into an element with explicit wait
     * @param element the WebElement to type into
     * @param text the text to type
     */
    protected void type(WebElement element, String text) {
        try {
            WebElement visibleElement = waitForVisibility(element);
            visibleElement.clear();
            visibleElement.sendKeys(text);
            log.debug("Typed '{}' into element: {}", text, element);
        } catch (Exception e) {
            log.error("Failed to type '{}' into element: {}", text, element, e);
            throw e;
        }
    }
    
    /**
     * Gets text from an element with explicit wait
     * @param element the WebElement to get text from
     * @return the text content of the element
     */
    protected String getText(WebElement element) {
        try {
            return waitForVisibility(element).getText();
        } catch (Exception e) {
            log.error("Failed to get text from element: {}", element, e);
            throw e;
        }
    }
    
    /**
     * Waits for a page to load completely
     */
    protected void waitForPageToLoad() {
        try {
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            log.debug("Page loaded completely");
        } catch (Exception e) {
            log.error("Page did not load completely", e);
            throw e;
        }
    }
    
    /**
     * Scrolls to an element using JavaScript
     * @param element the WebElement to scroll to
     */
    protected void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            log.debug("Scrolled to element: {}", element);
        } catch (Exception e) {
            log.error("Failed to scroll to element: {}", element, e);
            throw e;
        }
    }
    
    /**
     * Waits for an alert to be present
     * @return the Alert object
     */
    protected Alert waitForAlert() {
        try {
            return wait.until(ExpectedConditions.alertIsPresent());
        } catch (Exception e) {
            log.error("Alert is not present", e);
            throw e;
        }
    }

    protected WebElement waitForElementToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void clickElement(By locator) {
        waitForElementToBeClickable(locator).click();
    }

    protected String getElementText(By locator) {
        return waitForElementToBeVisible(locator).getText();
    }

    protected boolean isElementVisible(By locator) {
        try {
            return waitForElementToBeVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Refreshes the current page
     */
    public void refreshPage() {
        try {
            driver.navigate().refresh();
            waitForPageToLoad();
            log.info("Sayfa yenilendi");
        } catch (Exception e) {
            log.error("Sayfa yenilenemedi: {}", e.getMessage());
            throw e;
        }
    }
} 