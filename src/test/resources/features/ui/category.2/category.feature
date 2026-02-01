Feature: Category Management
  As an Admin
  I want to manage categories
  So that I can organize plants effectively

  @category @login_as_admin
  Scenario: CM2_UI_A_01 View Add Category Page
    When Admin navigates to the "Categories" page
    And Admin clicks the "Add A Category" button
    Then the "Add Category" page should open successfully

  @category @login_as_admin
  Scenario: CM2_UI_A_02 Validate Category Name Mandatory
    When Admin navigates to the "Categories" page
    And Admin clicks the "Add A Category" button
    And Admin leaves the Category Name empty
    And Admin clicks the Save button
    Then Admin should see validation message "Category name is required"

  @category @login_as_admin
  Scenario: CM2_UI_A_03 Validate Category Name Length
    When Admin navigates to the "Categories" page
    And Admin clicks the "Add A Category" button
    And Admin enters Category Name "AB"
    And Admin clicks the Save button
    Then Admin should see validation message "Category name must be between 3 and 10 characters"
