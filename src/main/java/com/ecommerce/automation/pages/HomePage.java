package com.ecommerce.automation.pages;

import com.ecommerce.automation.utils.ConfigReader;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Log4j2
public class HomePage extends BasePage {

    private final ConfigReader configReader = ConfigReader.getInstance();
    
    // Page elements
    @FindBy(id = "user-name")
    private WebElement usernameInput;
    
    @FindBy(id = "password")
    private WebElement passwordInput;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(css = ".inventory_item")
    private List<WebElement> productsList;
    
    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;
    
    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;
    
    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;
    
    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;
    
    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;
    
    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;
    
    // This locator helps identify add to cart buttons
    private final By addToCartButtonsLocator = By.cssSelector(".btn_inventory");
    
    /**
     * Constructor
     */
    public HomePage() {
        super();
    }
    
    /**
     * Navigates to the home page
     */
    public void navigateToHomePage() {
        navigateTo(configReader.getBaseUrl());
        log.info("Navigated to home page: {}", configReader.getBaseUrl());
    }
    
    /**
     * Logs in with the provided credentials
     * @param username the username
     * @param password the password
     */
    public void login(String username, String password) {
        log.info("Logging in with username: {}", username);
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        waitForPageToLoad();
    }
    
    /**
     * Gets the list of products
     * @return List of WebElements representing products
     */
    public List<WebElement> getProducts() {
        return productsList;
    }
    
    /**
     * Gets the product names
     * @return List of Strings with product names
     */
    public List<String> getProductNames() {
        return productNames.stream()
                .map(this::getText)
                .toList();
    }
    
    /**
     * Gets the product prices
     * @return List of Strings with product prices
     */
    public List<String> getProductPrices() {
        return productPrices.stream()
                .map(this::getText)
                .toList();
    }
    
    /**
     * Adds a product to the cart by its index
     * @param index the index of the product (0-based)
     */
    public void addProductToCart(int index) {
        if (index >= 0 && index < productsList.size()) {
            WebElement product = productsList.get(index);
            WebElement addToCartButton = product.findElement(addToCartButtonsLocator);
            scrollToElement(addToCartButton);
            click(addToCartButton);
            log.info("Added product to cart: {}", productNames.get(index).getText());
        } else {
            log.error("Invalid product index: {}", index);
            throw new IndexOutOfBoundsException("Invalid product index: " + index);
        }
    }
    
    /**
     * Adds a product to the cart by its name
     * @param productName the name of the product
     */
    public void addProductToCartByName(String productName) {
        for (int i = 0; i < productNames.size(); i++) {
            if (getText(productNames.get(i)).equals(productName)) {
                addProductToCart(i);
                return;
            }
        }
        log.error("Product not found: {}", productName);
        throw new IllegalArgumentException("Product not found: " + productName);
    }
    
    /**
     * Gets the number of items in the cart
     * @return the number of items in the cart
     */
    public int getCartItemCount() {
        try {
            return Integer.parseInt(getText(cartBadge));
        } catch (Exception e) {
            return 0; // No badge means empty cart
        }
    }
    
    /**
     * Clicks on the cart icon to navigate to the cart page
     */
    public void goToCart() {
        click(cartIcon);
        waitForPageToLoad();
        log.info("Navigated to cart page");
    }
    
    /**
     * Logs out
     */
    public void logout() {
        click(menuButton);
        waitForVisibility(logoutLink);
        click(logoutLink);
        waitForPageToLoad();
        log.info("Logged out successfully");
    }
} 