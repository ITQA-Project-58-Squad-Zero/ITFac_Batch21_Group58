Feature: View Sales
  As an Admin
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

