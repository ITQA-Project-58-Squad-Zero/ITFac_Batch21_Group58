Feature: Sales Management UI - View and Access Control
  As an authenticated user
  I want to view sales records and verify role-based access
  So that I can track plant sales transactions securely

  @sales @login_as_admin @SM_UI_A_01
  Scenario: SM_UI_A_01 View Sales List
    When Admin navigates to the "Sales" page
    Then Admin should see the sales table
    And the sales table should contain the following columns:
      | Plant       |
      | Quantity    |
      | Total Price |
      | Sold At     |

  @sales @login_as_admin @SM_UI_A_02
  Scenario: SM_UI_A_02 Sort Sales Records
    Given at least 2 sales records exist
    When Admin navigates to the "Sales" page
    When Admin clicks the "Plant" column header on the sales page
    Then records should be sorted by "Plant" in "ascending" order
    When Admin clicks the "Quantity" column header on the sales page
    Then records should be sorted by "Quantity" in "ascending" order
    When Admin clicks the "Total Price" column header on the sales page
    Then records should be sorted by "Total Price" in "ascending" order
    When Admin clicks the "Sold At" column header on the sales page
    Then records should be sorted by "Sold At" in "ascending" order

  @sales @login_as_admin @SM_UI_A_03
  Scenario: SM_UI_A_03 Verify Sell Plant Button Visibility
    When Admin navigates to the "Sales" page
    And Admin clicks the "Sell Plant" button
    Then the "Sell Plant" form should open successfully

  @sales @login_as_user @SM_UI_U_01
  Scenario: SM_UI_U_01 View Sales List (Non-Admin User)
    When User navigates to the "Sales" page
    Then User should see the sales table
    And the sales table should contain the following columns:
      | Plant       |
      | Quantity    |
      | Total Price |
      | Sold At     |
    And the "Sell Plant" button should not be visible

  @sales @login_as_user @SM_UI_U_02
  Scenario: SM_UI_U_02 Verify Sell Plant Button Is Hidden (Non-Admin User)
    When User navigates to the "Sales" page
    Then the "Sell Plant" button should not be visible
    And there should be no option to create a sale

  @sales @login_as_user @SM_UI_U_03
  Scenario: SM_UI_U_03 Verify Delete Action Is Hidden (Non-Admin User)
    When User navigates to the "Sales" page
    Then User should see the sales table
    And delete buttons should not be visible for any sales record

  @sales @login_as_user @SM_UI_U_05
  Scenario: SM_UI_U_05 Sort Sales Records (Non-Admin User)
    Given at least 2 sales records exist
    When User navigates to the "Sales" page
    When User clicks the "Sold At" column header on the sales page
    Then records should be sorted by "Sold At" in "ascending" order
    When User clicks the "Plant" column header on the sales page
    Then records should be sorted by "Plant" in "ascending" order


  @sales @login_as_user @SM_UI_U_04
  Scenario: SM_UI_U_04 Block Access to Sell Plant Page via Direct URL (Non-Admin User)
    When User attempts to access the Sell Plant page directly via URL
    Then User should be denied access or redirected
    And the Sell Plant form should not be accessible
