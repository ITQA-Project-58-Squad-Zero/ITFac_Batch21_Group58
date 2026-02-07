Feature: Category Management UI - Create and Update
  As an Admin
  I want to manage categories
  So that I can organize plants effectively

  @category @login_as_admin @CM2_UI_A_01
  Scenario: CM2_UI_A_01 View Add Category Page
    When Admin navigates to the "Categories" page
    And Admin clicks the "Add A Category" button
    Then the "Add Category" page should open successfully

  @category @login_as_admin @CM2_UI_A_02
  Scenario: CM2_UI_A_02 Validate Category Name Mandatory
    When Admin navigates to the "Categories" page
    And Admin clicks the "Add A Category" button
    And Admin leaves the Category Name empty
    And Admin clicks the Save button
    Then Admin should see validation message "Category name is required"

  @category @login_as_admin @CM2_UI_A_03
  Scenario: CM2_UI_A_03 Validate Category Name Length
    When Admin navigates to the "Categories" page
    And Admin clicks the "Add A Category" button
    And Admin enters Category Name "AB"
    And Admin clicks the Save button
    Then Admin should see validation message "Category name must be between 3 and 10 characters"

  @category @login_as_admin @CM2_UI_A_04
  Scenario: CM2_UI_A_04 Create Category via UI (Valid Data)
    When Admin navigates to the "Categories" page
    And Admin clicks the "Add A Category" button
    And Admin enters a unique Category Name
    And Admin clicks the Save button
    Then the unique category should be created successfully and appear in the category list

  @category @login_as_admin @CM2_UI_A_05
  Scenario: CM2_UI_A_05 Cancel Add/Edit Category (Admin)
    When Admin navigates to the "Categories" page
    And Admin clicks the "Add A Category" button
    And Admin clicks the Cancel button
    Then Admin should be redirected to the Categories list page

  @category @login_as_user @CM2_UI_U_01
  Scenario: CM2_UI_U_01 Verify Add Category UI Restriction (User)
    When User navigates to the "Categories" page
    Then the "Add A Category" button should not be visible

  @category @login_as_user @CM2_UI_U_02
  Scenario: CM2_UI_U_02 Block Direct URL Access to Add Category Page (User)
    When User attempts to access the Add Category page directly
    Then User should be redirected to Access Denied page or Login page

  @category @login_as_user @CM2_UI_U_03
  Scenario: CM2_UI_U_03 Verify Edit Category Restriction (User)
    When User navigates to the "Categories" page
    Then the Edit option should not be visible for any category

  @category @login_as_user @CM2_UI_U_04
  Scenario: CM2_UI_U_04 Block Category Update via UI (User)
    When User attempts to update an existing category
    Then the update action should be blocked

  @category @login_as_user @CM2_UI_U_05
  Scenario: CM2_UI_U_05 Block Category Deletion via UI (User)
    When User navigates to the "Categories" page
    Then the Delete option should not be visible for any category
