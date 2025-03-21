package com.ecommerce.automation.stepdefinitions;

import com.ecommerce.automation.pages.HepsiburadaHomePage;
import com.ecommerce.automation.pages.HepsiburadaProductPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;

public class HepsiburadaSteps {
    private final HepsiburadaHomePage homePage;
    private final HepsiburadaProductPage productPage;

    public HepsiburadaSteps() {
        homePage = new HepsiburadaHomePage();
        productPage = new HepsiburadaProductPage();
    }

    @Given("^open the website$")
    public void open_the_website() {
        homePage.navigateToHomePage();
    }

    @Then("^accept cookie$")
    public void accept_cookie() {
        homePage.acceptCookie();
    }

    @When("^hover on elektronik category$")
    public void hover_on_elektronik_category() {
        homePage.hoverOverElectronicsCategory();
    }

    @When("^clicks on Tablet subcategory$")
    public void clicks_on_tablet_subcategory() {
        homePage.clickTabletSubCategory();
    }

    @When("^selects Apple from brand filter$")
    public void selects_apple_from_brand_filter() {
        homePage.clickAppleBrandFilter();
    }

    @When("^clicks Apple brand filter$")
    public void clicks_apple_brand_filter() {
        homePage.clickAppleBrandFilter();
    }

    @When("^selects the specified screen size filter$")
    public void selects_the_specified_screen_size_filter() {
        productPage.selectScreenSizeFilter();
    }

    @When("^the user clicks on the most expensive product$")
    public void the_user_clicks_on_the_most_expensive_product() {
        productPage.clickMostExpensiveProduct();
    }

    @When("^the user clicks add to cart button$")
    public void the_user_clicks_add_to_cart_button() {
        productPage.clickAddToCart();
    }

    @Then("^the user should see product added to cart message$")
    public void the_user_should_see_product_added_to_cart_message() {
        Assert.assertTrue("Ürün sepete eklenemedi", productPage.isProductAddedToCart());
    }

    @When("^the user refreshes the page$")
    public void the_user_refreshes_the_page() {
        homePage.refreshThePage();
    }

    @Then("^go to basket and check the product price in cart should match the product detail page price$")
    public void go_to_basket_and_check_price_match() {
        Assert.assertTrue("Sepetteki ürün fiyatı, ürün detay sayfasındaki fiyatla eşleşmiyor", productPage.isPriceMatchingInCart());
    }
}