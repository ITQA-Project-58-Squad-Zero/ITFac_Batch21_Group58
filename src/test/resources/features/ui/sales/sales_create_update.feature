Feature: Sales Management UI - Create Sale
  As an Admin
  I want to create new sales
  So that I can record plant sale transactions

  @sales @login_as_admin @SM_UI_A_04
  Scenario: SM_UI_A_04 Create Sale via UI using Valid Data
    When Admin navigates to the "Sales" page
    And Admin clicks the "Sell Plant" button
    And Admin selects a plant from the dropdown
    And Admin enters quantity "1"
    And Admin clicks the "Sell" button
    Then Admin should be on the Sales list page
    And the newly created sale should appear in the sales table

  @sales @login_as_admin @SM_UI_A_05
  Scenario Outline: SM_UI_A_05 Validate Sell Plant Form for Invalid Quantity
    When Admin navigates to the "Sales" page
    And Admin clicks the "Sell Plant" button
    And Admin selects a plant from the dropdown
    And Admin enters quantity "<quantity>"
    And Admin clicks the "Sell" button
    Then a validation error message should be displayed
    And the sale should not be created

    Examples:
      | quantity |
      |        0 |
      |          |
