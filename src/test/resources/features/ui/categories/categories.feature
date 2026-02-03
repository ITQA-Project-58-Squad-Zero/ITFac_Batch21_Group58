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
    And pagination controls should be visible
    When Admin clicks the next page in categories
    Then the current page number should change
    And the category records should update
