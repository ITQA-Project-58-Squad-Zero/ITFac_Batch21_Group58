Feature: View Sales
  As an authenticated user
  I want to view sales records
  So that I can track all plant sales transactions

  @sales @login_as_admin
  Scenario: SM_UI_A_01 View Sales List
    When Admin navigates to the "Sales" page
    Then Admin should see the sales table
    And the sales table should contain the following columns:
      | Plant       |
      | Quantity    |
      | Total Price |
      | Sold At     |

  @sales @login_as_admin
  Scenario: SM_UI_A_03 Verify Sell Plant Button Visibility
    When Admin navigates to the "Sales" page
    And Admin clicks the "Sell Plant" button
    Then the "Sell Plant" form should open successfully

  @sales @login_as_user
  Scenario: SM_UI_U_01 View Sales List (Non-Admin User)
    When User navigates to the "Sales" page
    Then User should see the sales table
    And the sales table should contain the following columns:
      | Plant       |
      | Quantity    |
      | Total Price |
      | Sold At     |
    And the "Sell Plant" button should not be visible

  @sales @login_as_user
  Scenario: SM_UI_U_03 Verify Delete Action Is Hidden (Non-Admin User)
    When User navigates to the "Sales" page
    Then User should see the sales table
    And delete buttons should not be visible for any sales record

  @sales @login_as_user
  Scenario: SM_UI_U_04 Block Access to Sell Plant Page via Direct URL (Non-Admin User)
    When User attempts to access the Sell Plant page directly via URL
    Then User should be denied access or redirected
    And the Sell Plant form should not be accessible
