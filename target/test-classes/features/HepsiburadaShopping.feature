Feature: Hepsiburada Shopping

  @e2e
  Scenario: User can filter and add most expensive Apple tablet to cart
    Given open the website
    Then accept cookie
    When hover on elektronik category
    And clicks on Tablet subcategory
    And selects Apple from brand filter
    And selects the specified screen size filter
    And clicks Apple brand filter
    And the user refreshes the page
    And the user clicks on the most expensive product
    And the user clicks add to cart button
    Then the user should see product added to cart message
    And go to basket and check the product price in cart should match the product detail page price