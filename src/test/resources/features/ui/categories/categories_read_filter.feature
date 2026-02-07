@ui
Feature: Category Management UI - View and Filter
  As a User or Admin
  I want to view the category list and (for admin) use pagination
  So that I can verify list display and role-based actions

  @categories @login_as_user @CM1_UI_U_001
  Scenario: CM1_UI_U_001 View Category List (Non-Admin User)
    When User navigates to the "Categories" page
    Then the categories table should be displayed
    And the categories table should contain the following columns:
      | ID     |
      | Name   |
      | Parent |

  @categories @login_as_user @CM1_UI_U_002
  Scenario: CM1_UI_U_002 Edit/Delete Disabled or Hidden (Non-Admin User)
    When User navigates to the "Categories" page
    Then the categories table should be displayed
    And Edit and Delete actions should be disabled for non-admin user

  @categories @login_as_admin @CM1_UI_A_001
  Scenario: CM1_UI_A_001 View Category List + Pagination (Admin)
    When Admin navigates to the "Categories" page
    Then the categories table should be displayed
    And the categories table should contain the following columns:
      | ID     |
      | Name   |
      | Parent |
    And category pagination controls should be visible
    When Admin clicks the next page in categories
    Then the current page number should change
    And the category records should update

  @categories @login_as_admin @CM1_UI_A_002
  Scenario: CM1_UI_A_002 Add Category Button Visible (Admin)
    When Admin navigates to the "Categories" page
    Then the category "Add Category" button should be visible

  @categories @login_as_admin @CM1_UI_A_003
  Scenario: CM1_UI_A_003 Search by Category Name (Admin)
    Given Admin retrieves the first available category name
    When Admin navigates to the "Categories" page
    And Admin enters the first available category name in the category search box
    And Admin clicks the search button
    Then the categories table should show only matching results for the searched category name

  @categories @login_as_admin @CM1_UI_A_004
  Scenario: CM1_UI_A_004 Filter by Parent Category (Admin)
    Given Admin retrieves a parent category from the system
    When Admin navigates to the "Categories" page
    And Admin selects the retrieved parent category from the filter dropdown
    And Admin clicks the search button
    Then the categories table should show only categories under the selected parent

  @categories @login_as_admin @CM1_UI_A_005
  Scenario: CM1_UI_A_005 Sorting by ID / Name / Parent (Admin) (ASC/DESC)
    When Admin navigates to the "Categories" page
    And Admin clicks the category "ID" column header
    Then the categories should be sorted by ID in ascending order
    When Admin clicks the category "ID" column header again
    Then the categories should be sorted by ID in descending order
    When Admin clicks the category "Name" column header
    Then the categories should be sorted by Name in ascending order
    When Admin clicks the category "Name" column header again
    Then the categories should be sorted by Name in descending order
    When Admin clicks the category "Parent" column header
    Then the categories should be sorted by Parent in ascending order
    When Admin clicks the category "Parent" column header again
    Then the categories should be sorted by Parent in descending order

  @categories @login_as_admin @CM1_UI_003
  Scenario: CM1_UI_003 No Categories Found Message
    When Admin navigates to the "Categories" page
    And Admin searches for a category that does not exist
    Then the "No category found" message should be displayed

  @categories @login_as_admin @CM1_UI_004
  Scenario: CM1_UI_004 Pagination Default Load
    When Admin navigates to the "Categories" page
    Then the categories table should be displayed
    And the initial page should show limited rows based on page size
    And category pagination controls should be visible

  @categories @login_as_user @CM1_UI_U_003
  Scenario: CM1_UI_U_003 Pagination Next/Previous Navigation (Non-Admin User)
    When User navigates to the "Categories" page
    And User notes the current page number and first row data
    When User clicks the next page in categories
    Then the page number should change
    And the category rows should change
    When User clicks the previous page in categories
    Then the page number should return to the original page
    And the category rows should return to original

  @categories @login_as_admin @CM1_UI_A_006
  Scenario: CM1_UI_A_006 Search by Category Name – No Match (Admin)
    When Admin navigates to the "Categories" page
    And Admin enters a non-existing keyword in the category search box
    And Admin clicks the search button
    Then the "No category found" message should be displayed
    And no category rows should be displayed

  @categories @login_as_user @CM1_UI_U_004
  Scenario: CM1_UI_U_004 Category Search Reset/Clear (Non-Admin User)
    Given User retrieves the first available category name
    When User navigates to the "Categories" page
    And User notes the total number of categories displayed
    When User enters the first available category name in the category search box
    And User clicks the search button
    Then the categories table should show filtered results
    When User clears the search text
    And User clicks the search button
    Then the full category list should be displayed again

  @categories @login_as_admin @CM1_UI_A_007
  Scenario: CM1_UI_A_007 Filter by Parent Category + Clear Filter (Admin)
    Given Admin retrieves a parent category from the system
    When Admin navigates to the "Categories" page
    And Admin notes the total number of categories displayed
    When Admin selects the retrieved parent category from the filter dropdown
    And Admin clicks the search button
    Then the categories table should show only categories under the selected parent
    When Admin clears the parent filter
    And Admin clicks the search button
    Then the full category list should be displayed again

  @categories @login_as_admin @CM1_UI_A_008
  Scenario: CM1_UI_A_008 Combined Search + Parent Filter (Admin)
    Given Admin retrieves a parent category from the system
    And Admin retrieves a subcategory name under the selected parent
    When Admin navigates to the "Categories" page
    When Admin selects the retrieved parent category from the filter dropdown
    And Admin enters the retrieved subcategory name in the category search box
    And Admin clicks the search button
    Then the categories table should show results matching both the selected parent and the searched keyword

  @categories @login_as_user @CM1_UI_U_005
  Scenario: CM1_UI_U_005 Sorting Works With Parent Null Values (Non-Admin User)
    When User navigates to the "Categories" page
    When User clicks the category "Parent" column header
    Then the categories should be sorted by Parent in ascending order
    And sorting should handle null parent values correctly
    When User clicks the category "Parent" column header again
    Then the categories should be sorted by Parent in descending order
    And sorting should handle null parent values correctly

  @categories @login_as_user @CM1_UI_U_006
  Scenario: CM1_UI_U_006 Non-Admin Cannot Access Add Category Action
    When User navigates to the "Categories" page
    Then the category "Add Category" button should not be visible
