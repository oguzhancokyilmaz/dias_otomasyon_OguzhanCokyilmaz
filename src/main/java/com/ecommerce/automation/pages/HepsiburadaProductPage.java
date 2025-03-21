package com.ecommerce.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

public class HepsiburadaProductPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(HepsiburadaProductPage.class);
    
    // Updated locators for Hepsiburada
    private final By brandFilter = By.cssSelector("div[id='containerSelectorFocus'] div[class='carousel-hCHe_h9y71imemt_TLIg'] div:nth-child(1) a:nth-child(1) span:nth-child(1)");
    private final By screenSizeFilter = By.xpath("//*[@href='/tablet-c-3008012?filtreler=ekranboyutu:13€2C2€20in€C3€A7']");
    private final By productPrices = By.cssSelector("[data-test-id=price-current-price]");
    private final By addToCartButton = By.cssSelector("[data-test-id=variant-add-to-cart-button]");
    private final By productPrice = By.cssSelector("[data-test-id=price-current-price]");
    private final By addToCartSuccessMessage = By.cssSelector(".hb-toast-text");
    private final By cartPrice = By.cssSelector(".product_price_uXU6Q");
    private final By goToCartButton = By.id("shoppingCart");

    private String savedProductPrice;

    public void selectBrandFilter() {
        try {
            // Element görünür olana kadar bekle
            WebElement appleBrand = waitForElementToBeVisible(brandFilter);
            
            // Element tıklanabilir olana kadar bekle
            waitForElementToBeClickable(brandFilter);
            
            // Elemente tıkla
            appleBrand.click();
            
            // Sayfa yüklenene kadar bekle
            waitForPageToLoad();
            
            // 1 saniye beklemek için WebDriverWait kullanma (Thread.sleep yerine)
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(1));
            shortWait.until(webDriver -> {
                try {
                    return ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete");
                } catch (Exception e) {
                    return false;
                }
            });
            
            logger.info("Apple marka filtresi seçildi");
        } catch (Exception e) {
            logger.error("Apple marka filtresini seçerken hata oluştu: {}", e.getMessage());
        }
    }

    public void selectScreenSizeFilter() {
        try {
            // Doğrudan ekran boyutu bağlantısına tıklayalım
            clickElement(screenSizeFilter);
            waitForPageToLoad();
            logger.info("13.2 inç ekran boyutu filtresi seçildi");
        } catch (Exception e) {
            logger.error("Ekran boyutu filtresini seçerken hata oluştu: {}", e.getMessage());
        }
    }

    public void clickMostExpensiveProduct() {
        try {
            logger.info("En pahalı ürün aranıyor ve tıklanacak...");
            
            // Fiyat ID'lerini tanımla (# işareti olmadan)
            String[] priceIds = {"final-price-1", "final-price-2", "final-price-3", "final-price-4"};
            
            int maxPriceIndex = -1;
            double maxPrice = -1;
            String maxPriceId = "";
            
            // Fiyatları kontrol edip en yüksek olanı bulalım
            for (int i = 0; i < priceIds.length; i++) {
                String idValue = priceIds[i];
                // CSS seçici oluştur - data-test-id ile elementi bul
                By priceSelector = By.cssSelector("[data-test-id=" + idValue + "]");
                
                try {
                    // Element var mı kontrol et
                    if (isElementPresent(priceSelector)) {
                        WebElement element = waitForElementToBeVisible(priceSelector);
                        // Fiyat metnini al ve temizle (sadece sayıları bırak, virgülü noktaya çevir)
                        String priceText = element.getText().replaceAll("[^0-9,]", "").replace(",", ".");
                        logger.info("Ürün {}: Ham fiyat metni = {}", i+1, element.getText());
                        logger.info("Ürün {}: Temizlenmiş fiyat metni = {}", i+1, priceText);
                        
                        try {
                            double price = Double.parseDouble(priceText);
                            logger.info("Ürün {} ({}): Fiyat = {}", i+1, idValue, price);
                            
                            // En yüksek fiyatı güncelle
                            if (price > maxPrice) {
                                maxPrice = price;
                                maxPriceIndex = i;
                                maxPriceId = idValue;
                            }
                        } catch (NumberFormatException e) {
                            logger.error("Fiyat dönüştürülürken hata oluştu ({}): {}", idValue, e.getMessage());
                        }
                    } else {
                        logger.warn("Fiyat elementi bulunamadı: {}", idValue);
                    }
                } catch (Exception e) {
                    logger.error("Fiyat elementi işlenirken hata oluştu ({}): {}", idValue, e.getMessage());
                }
            }
            
            if (maxPriceIndex != -1) {
                logger.info("En yüksek fiyat bulundu: ID={}, Fiyat={}", maxPriceId, maxPrice);
                
                // Doğrudan en yüksek fiyatlı elemana tıkla
                By priceSelector = By.cssSelector("[data-test-id=" + maxPriceId + "]");
                
                try {
                    WebElement priceElement = waitForElementToBeClickable(priceSelector);
                    
                    // JavaScript ile tıklama yap
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", priceElement);
                    
                    logger.info("En pahalı fiyat elementine tıklandı. ID: {}, Fiyat: {}", maxPriceId, maxPrice);
                } catch (Exception e) {
                    logger.error("Fiyat elementine tıklarken hata oluştu: {}", e.getMessage());
                    
                    // Alternatif olarak, elementin en yakın ürün kartını bulmaya çalış
                    try {
                        WebElement element = driver.findElement(priceSelector);
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        WebElement productCard = (WebElement) js.executeScript(
                            "return arguments[0].closest('.product-card-container');", 
                            element
                        );
                        
                        if (productCard != null) {
                            js.executeScript("arguments[0].click();", productCard);
                            logger.info("Fiyat elementinden bulunan ürün kartına tıklandı. Fiyat: {}", maxPrice);
                        } else {
                            logger.error("Ürün kartı elementine ulaşılamadı");
                        }
                    } catch (Exception ex) {
                        logger.error("Alternatif tıklama da başarısız oldu: {}", ex.getMessage());
                    }
                }
            } else {
                logger.error("En pahalı ürün bulunamadı");
            }
            
            waitForPageToLoad();
        } catch (Exception e) {
            logger.error("En pahalı ürüne tıklarken hata oluştu: {}", e.getMessage());
        }
    }

    public void clickAddToCart() {
        try {
            logger.info("Sepete ekle butonuna tıklanıyor...");
            
            // Önce ürün fiyatını kaydet
            try {
                savedProductPrice = getElementText(productPrice);
                logger.info("Ürün fiyatı kaydedildi: {}", savedProductPrice);
            } catch (Exception e) {
                logger.warn("Ürün fiyatı kaydedilemedi: {}", e.getMessage());
            }
            
            // Sepete ekle butonuna tıkla
            if (isElementPresent(addToCartButton)) {
                WebElement addToCartElement = waitForElementToBeClickable(addToCartButton);
                
                // JavaScript ile tıklama yap (daha güvenilir)
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", addToCartElement);
                
                // Sayfanın yüklenmesini bekle
                waitForPageToLoad();
                
                logger.info("Ürün sepete eklendi");
            } else {
                logger.error("Sepete ekle butonu bulunamadı");
            }
        } catch (Exception e) {
            logger.error("Ürünü sepete eklerken hata oluştu: {}", e.getMessage());
        }
    }

    public boolean isProductAddedToCart() {
        try {
            logger.info("Ürünün sepete eklenip eklenmediği kontrol ediliyor...");
            
            // CSS Selector ile toast mesajını bul
            WebElement toastElement = waitForElementToBeVisible(addToCartSuccessMessage);
            
            // Toast mesajının içeriğini al ve başındaki/sonundaki boşlukları temizle
            String toastMessage = toastElement.getText().trim();
            logger.info("Toast mesajı: '{}'", toastMessage);
            
            // Beklenen metin ile karşılaştır
            boolean isAdded = "Ürün sepete eklendi".equals(toastMessage);
            
            logger.info("Ürün sepete eklendi mi?: {}", isAdded);
            return isAdded;
        } catch (Exception e) {
            logger.error("Ürünün sepete eklenip eklenmediği kontrol edilirken hata oluştu: {}", e.getMessage());
            return false;
        }
    }

    public void goToCart() {
        try {
            logger.info("Sepete gidiliyor...");
            
            // Sepet butonunu bul ve tıkla
            if (isElementPresent(goToCartButton)) {
                WebElement cartButton = waitForElementToBeClickable(goToCartButton);
                
                // JavaScript ile tıklama yap
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", cartButton);
                
                // Sayfanın yüklenmesini bekle
                waitForPageToLoad();
                
                logger.info("Sepet sayfasına gidildi");
            } else {
                logger.error("Sepet butonu bulunamadı");
            }
        } catch (Exception e) {
            logger.error("Sepete giderken hata oluştu: {}", e.getMessage());
        }
    }

    public boolean isPriceMatchingInCart() {
        try {
            logger.info("Sepetteki fiyat ile ürün sayfasındaki fiyat karşılaştırılıyor...");
            
            // Önce sepete git
            goToCart();
            
            // Sepetteki fiyat elementini bul
            WebElement cartPriceElement = waitForElementToBeVisible(cartPrice);
            String cartPriceText = cartPriceElement.getText().trim();
            
            logger.info("Sepetteki fiyat: '{}', Kaydedilen ürün fiyatı: '{}'", cartPriceText, savedProductPrice);
            
            // Fiyatları temizle: TL, boşluk ve diğer sembollerden arındır, virgülü nokta ile değiştir
            String cleanCartPrice = cartPriceText.replaceAll("[^0-9,]", "").replace(",", ".");
            String cleanProductPrice = savedProductPrice.replaceAll("[^0-9,]", "").replace(",", ".");
            
            // String'leri double'a çevir
            double cartPriceValue = Double.parseDouble(cleanCartPrice);
            double productPriceValue = Double.parseDouble(cleanProductPrice);
            
            // Double değerleri karşılaştır
            boolean isMatching = cartPriceValue == productPriceValue;
            
            logger.info("Temizlenmiş fiyatlar - Sepet: {}, Ürün: {}", cartPriceValue, productPriceValue);
            logger.info("Fiyatlar eşleşiyor mu? {}", isMatching);
            return isMatching;
        } catch (Exception e) {
            logger.error("Fiyatları karşılaştırırken hata oluştu: {}", e.getMessage());
            return false;
        }
    }
} 