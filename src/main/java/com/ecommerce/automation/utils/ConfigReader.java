package com.ecommerce.automation.utils;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class ConfigReader {
    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                log.error("config.properties dosyası bulunamadı!");
                throw new RuntimeException("config.properties dosyası bulunamadı!");
            }
            properties.load(input);
            log.info("Configuration dosyası başarıyla yüklendi");
        } catch (IOException e) {
            log.error("Configuration dosyası yüklenirken hata oluştu", e);
            throw new RuntimeException("Configuration dosyası yüklenirken hata oluştu", e);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public String getBrowserType() {
        return properties.getProperty("browser.type", "chrome");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("browser.headless", "false"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.page.load", "30"));
    }

    public int getElementWaitTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.element.wait", "10"));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}