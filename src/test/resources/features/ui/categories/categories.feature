Feature: Categories Management
  As a User or Admin
  I want to view the category list and (for admin) use pagination
  So that I can verify list display and role-based actions

  @categories @login_as_user @TC004
  Scenario: TC004 View Category List (Non-Admin User)
    When User navigates to the "Categories" page
    Then the categories table should be displayed
    And the categories table should contain the following columns:
      | ID     |
      | Name   |
      | Parent |

  @categories @login_as_user @TC005
  Scenario: TC005 Edit/Delete Disabled or Hidden (Non-Admin User)
    When User navigates to the "Categories" page
    Then the categories table should be displayed
    And Edit and Delete actions should be disabled for non-admin user

  @categories @login_as_admin @TC006
  Scenario: TC006 View Category List and Pagination (Admin)
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

  @categories @login_as_admin @TC007
  Scenario: TC007 Add Category Button Visible (Admin)
    When Admin navigates to the "Categories" page
    Then the category "Add Category" button should be visible

  @categories @login_as_admin @TC008
  Scenario: TC008 Search by Category Name (Admin)
    When Admin navigates to the "Categories" page
    And Admin enters "{string}" in the category search box
    And Admin clicks the search button
    Then the categories table should show only matching results for "{string}"

  @categories @login_as_admin @TC009
  Scenario Outline: TC009 Filter by Parent Category (Admin)
    When Admin navigates to the "Categories" page
    And Admin selects parent category "<parentName>" from the filter dropdown
    And Admin clicks the search button
    Then the categories table should show only categories under parent "<parentName>"

    Examples:
      | parentName |
      | Indoor     |

  @categories @login_as_admin @TC010
  Scenario: TC010 Sorting by ID Name Parent (Admin)
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

  @categories @login_as_admin @TC011
  Scenario: TC011 No Categories Found Message
    When Admin navigates to the "Categories" page
    And Admin searches for a category that does not exist
    Then the "No category found" message should be displayed

  @categories @login_as_admin @TC013
  Scenario: TC013 Pagination Default Load
    When Admin navigates to the "Categories" page
    Then the categories table should be displayed
    And the initial page should show limited rows based on page size
    And category pagination controls should be visible

  @categories @login_as_user @TC014
  Scenario: TC014 Pagination Next Previous Navigation (Non-Admin User)
    When User navigates to the "Categories" page
    And User notes the current page number and first row data
    When User clicks the next page in categories
    Then the page number should change
    And the category rows should change
    When User clicks the previous page in categories
    Then the page number should return to the original page
    And the category rows should return to original

  @categories @login_as_admin @TC015
  Scenario: TC015 Search by Category Name No Match (Admin)
    When Admin navigates to the "Categories" page
    And Admin enters a non-existing keyword in the category search box
    And Admin clicks the search button
    Then the "No category found" message should be displayed
    And no category rows should be displayed

  @categories @login_as_user @TC016
  Scenario: TC016 Category Search Reset Clear (Non-Admin User)
    When User navigates to the "Categories" page
    And User notes the total number of categories displayed
    When User enters "Indoor" in the category search box
    And User clicks the search button
    Then the categories table should show filtered results
    When User clears the search text
    And User clicks the search button
    Then the full category list should be displayed again

  @categories @login_as_admin @TC017
  Scenario Outline: TC017 Filter by Parent Category Clear Filter (Admin)
    When Admin navigates to the "Categories" page
    And Admin notes the total number of categories displayed
    When Admin selects parent category "<parentName>" from the filter dropdown
    And Admin clicks the search button
    Then the categories table should show only categories under parent "<parentName>"
    When Admin clears the parent filter
    And Admin clicks the search button
    Then the full category list should be displayed again

    Examples:
      | parentName |
      | Indoor     |

  @categories @login_as_admin @TC018
  Scenario Outline: TC018 Combined Search Parent Filter (Admin)
    When Admin navigates to the "Categories" page
    When Admin selects parent category "<parentName>" from the filter dropdown
    And Admin enters "<keyword>" in the category search box
    And Admin clicks the search button
    Then the categories table should show results matching both parent "<parentName>" and keyword "<keyword>"

    Examples:
      | parentName | keyword  |
      | Indoor     | Monstera |

  @categories @login_as_user @TC019
  Scenario: TC019 Sorting Works With Parent Null Values (Non-Admin User)
    When User navigates to the "Categories" page
    When User clicks the category "Parent" column header
    Then the categories should be sorted by Parent in ascending order
    And sorting should handle null parent values correctly
    When User clicks the category "Parent" column header again
    Then the categories should be sorted by Parent in descending order
    And sorting should handle null parent values correctly

  @categories @login_as_user @TC020
  Scenario: TC020 Non-Admin Cannot Access Add Category Action
    When User navigates to the "Categories" page
    Then the category "Add Category" button should not be visible
