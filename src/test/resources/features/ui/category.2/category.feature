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

  @category @login_as_admin
  Scenario: CM2_UI_A_04 Create Category via UI (Valid Data)
    When Admin navigates to the "Categories" page
    And Admin clicks the "Add A Category" button
    And Admin enters Category Name "TestCat"
    And Admin clicks the Save button
    Then Category "TestCat" should be created successfully and appear in the category list

  @category @login_as_admin
  Scenario: CM2_UI_A_05 Cancel Add/Edit Category (Admin)
    When Admin navigates to the "Categories" page
    And Admin clicks the "Add A Category" button
    And Admin clicks the Cancel button
    Then Admin should be redirected to the Categories list page

  @category @login_as_user
  Scenario: CM2_UI_U_01 Verify Add Category UI Restriction (User)
    When Admin navigates to the "Categories" page
    Then the "Add A Category" button should not be visible

  @category @login_as_user
  Scenario: CM2_UI_U_02 Block Direct URL Access to Add Category Page (User)
    When User attempts to access the Add Category page directly
    Then User should be redirected to Access Denied page or Login page
