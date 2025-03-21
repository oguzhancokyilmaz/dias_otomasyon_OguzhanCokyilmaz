package com.ecommerce.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.ecommerce.automation.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

public class HepsiburadaHomePage extends BasePage {
    private static final Logger logger = LogManager.getLogger(HepsiburadaHomePage.class);
    private final ConfigReader configReader = ConfigReader.getInstance();

    // Updated locators for Hepsiburada
    private final By acceptCookieButton = By.id("onetrust-accept-btn-handler");
    private final By electronicsCategory = By.xpath("(//span[@class='sf-MenuItems-UHHCg2qrE5_YBqDV_7AC'])[1]");
    private final By tabletSubCategory = By.xpath("//a[contains(text(),'Tablet')]");

    public void navigateToHomePage() {
        driver.get(configReader.getBaseUrl());
        waitForPageToLoad();
        logger.info("Ana sayfaya yönlendirildi: {}", configReader.getBaseUrl());
    }

    public void acceptCookie() {
        try {
            if (isElementPresent(acceptCookieButton)) {
                clickElement(acceptCookieButton);
                logger.info("Çerez kabul butonu tıklandı");
            } else {
                logger.info("Çerez kabul butonu bulunamadı veya görünür değil");
            }
        } catch (Exception e) {
            logger.error("Çerez kabul butonuna tıklarken hata oluştu: {}", e.getMessage());
        }
    }

    public void hoverOverElectronicsCategory() {
        try {
            WebElement element = waitForElementToBeVisible(electronicsCategory);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            logger.info("Elektronik kategorisi üzerine gidildi");
        } catch (Exception e) {
            logger.error("Elektronik kategorisi üzerine giderken hata oluştu: {}", e.getMessage());
        }
    }

    public void clickElectronicsCategory() {
        try {
            clickElement(electronicsCategory);
            logger.info("Elektronik kategorisine tıklandı");
            // Sayfanın yüklenmesini bekle
            waitForPageToLoad();
        } catch (Exception e) {
            logger.error("Elektronik kategorisine tıklarken hata oluştu: {}", e.getMessage());
        }
    }

    public void clickAppleBrandFilter() {
        try {
            // Apple brand filter için XPath locator
            By appleFilter = By.xpath("//*[@href='/apple/tablet-c-3008012']");
            
            // Sayfanın tamamen yüklenmesini bekle
            waitForPageToLoad();
            
            // 3 saniye bekle
            WebDriverWait threeSecondWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            
            // Element görünene kadar bekle
            WebElement element = waitForElementToBeVisible(appleFilter);
            logger.info("Apple marka filtresi görünür hale geldi");
            
            // Element tıklanabilir olana kadar bekle
            element = waitForElementToBeClickable(appleFilter);
            logger.info("Apple marka filtresi tıklanabilir hale geldi");
            
            // JavaScript ile tıklama yaparak olası stale element hatasını önle
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            
            // Sayfanın yüklenmesini bekle
            waitForPageToLoad();
            
            logger.info("Apple marka filtresine tıklandı");
        } catch (Exception e) {
            logger.error("Apple marka filtresine tıklarken hata oluştu: {}", e.getMessage());
        }
    }

    public void clickTabletSubCategory() {
        try {
            clickElement(tabletSubCategory);
            logger.info("Tablet alt kategorisine tıklandı");
        } catch (Exception e) {
            logger.error("Tablet alt kategorisine tıklarken hata oluştu: {}", e.getMessage());
        }
    }

    public void refreshThePage() {
        try {
            refreshPage();
            logger.info("Hepsiburada sayfası yenilendi");
        } catch (Exception e) {
            logger.error("Hepsiburada sayfası yenilenirken hata oluştu: {}", e.getMessage());
        }
    }
}