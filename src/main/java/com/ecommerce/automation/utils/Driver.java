package com.ecommerce.automation.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Log4j2
public class Driver {

    // Thread local for parallel execution
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> waitThreadLocal = new ThreadLocal<>();
    private static final ConfigReader configReader = ConfigReader.getInstance();

    public enum BrowserType {
        CHROME,
        FIREFOX
    }

    private Driver() {
        // Private constructor to prevent instantiation
    }

    public static WebDriver getDriver() {
        return getDriver(null);
    }

    public static WebDriver getDriver(BrowserType browserType) {
        if (driverThreadLocal.get() == null) {
            // Eğer browser tipi belirtilmemişse, config dosyasından oku
            if (browserType == null) {
                browserType = getBrowserType();
            }
            
            try {
                switch (browserType) {
                    case CHROME:
                        initChromeDriver();
                        break;
                    case FIREFOX:
                        initFirefoxDriver();
                        break;
                    default:
                        initChromeDriver();
                }
                
                WebDriver driver = driverThreadLocal.get();
                driver.manage().window().maximize();
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(configReader.getPageLoadTimeout()));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(configReader.getElementWaitTimeout()));
                
                // Initialize WebDriverWait with the configured timeout
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(configReader.getElementWaitTimeout()));
                waitThreadLocal.set(wait);
                
                log.info("Driver initialized with browser: {}", browserType);
            } catch (Exception e) {
                log.error("Browser başlatılamadı: {} - Hata: {}", browserType, e.getMessage());
                throw e; // Hata durumunda hata fırlat
            }
        }
        
        return driverThreadLocal.get();
    }

    private static BrowserType getBrowserType() {
        // Sistem özelliğinden browser tipini kontrol et
        String systemPropertyBrowser = System.getProperty("browser.type");
        
        if (systemPropertyBrowser != null) {
            if (systemPropertyBrowser.equalsIgnoreCase("firefox")) {
                log.info("Sistem özelliğinden Firefox browser tipi seçildi");
                return BrowserType.FIREFOX;
            } else if (systemPropertyBrowser.equalsIgnoreCase("chrome")) {
                log.info("Sistem özelliğinden Chrome browser tipi seçildi");
                return BrowserType.CHROME;
            }
        }
        
        // Eğer sistem özelliği yoksa config dosyasından oku
        String configBrowser = configReader.getBrowserType().toLowerCase();
        if (configBrowser.equals("firefox")) {
            log.info("Config dosyasından Firefox browser tipi seçildi");
            return BrowserType.FIREFOX;
        } else {
            log.info("Config dosyasından Chrome browser tipi seçildi");
            return BrowserType.CHROME;
        }
    }

    private static void initChromeDriver() {
        try {
            log.info("Chrome driver başlatılıyor...");
            
            WebDriverManager.chromedriver().setup();
            log.info("ChromeDriver WebDriverManager tarafından kuruldu");
            
            // En basit haliyle Chrome options
            ChromeOptions options = new ChromeOptions();
            
            // Temel güvenli seçenekler
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            
            log.info("ChromeDriver oluşturuluyor...");
            WebDriver driver = new ChromeDriver(options);
            log.info("ChromeDriver başarıyla oluşturuldu");
            
            driverThreadLocal.set(driver);
            log.info("Chrome driver ThreadLocal'e eklendi");
        } catch (Exception e) {
            log.error("Chrome driver başlatılamadı: {}", e.getMessage(), e);
            throw new RuntimeException("Chrome driver başlatılamadı", e);
        }
    }

    private static void initFirefoxDriver() {
        try {
            log.info("Firefox driver başlatılıyor...");
            
            // WebDriverManager kullanarak GeckoDriver'ı otomatik olarak indir
            WebDriverManager.firefoxdriver().setup();
            log.info("GeckoDriver WebDriverManager tarafından kuruldu");
            
            // Firefox options
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--disable-notifications");
            
            // Firefox binary ayarını yalnızca Firefox klasörünün varlığı doğrulanmışsa ekle
            File firefoxBinary = new File("C:\\Firefox\\Firefox\\firefox.exe");
            if (firefoxBinary.exists()) {
                options.setBinary(firefoxBinary.getAbsolutePath());
                log.info("Firefox binary ayarlandı: {}", firefoxBinary.getAbsolutePath());
            } else {
                log.info("Firefox binary belirtilen konumda bulunamadı, varsayılan konum kullanılacak");
            }
            
            // Güvenli olmayan sertifikaları kabul et
            options.setAcceptInsecureCerts(true);
            
            log.info("FirefoxDriver oluşturuluyor...");
            WebDriver driver = new FirefoxDriver(options);
            log.info("FirefoxDriver başarıyla oluşturuldu");
            
            driverThreadLocal.set(driver);
            log.info("Firefox driver ThreadLocal'e eklendi");
        } catch (Exception e) {
            log.error("Firefox driver başlatılamadı: {}", e.getMessage(), e);
            throw new RuntimeException("Firefox driver başlatılamadı", e);
        }
    }

    public static WebDriverWait getWait() {
        return waitThreadLocal.get();
    }

    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            try {
                driverThreadLocal.get().quit();
                log.info("WebDriver quit successfully");
            } catch (Exception e) {
                log.error("Error while quitting WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
                waitThreadLocal.remove();
            }
        }
    }
} 