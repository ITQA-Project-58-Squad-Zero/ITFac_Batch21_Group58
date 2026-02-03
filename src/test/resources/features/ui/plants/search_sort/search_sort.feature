@Plants @login_as_admin
Feature: Plants Search and Sort Functionality

  @Plants
  Scenario: TC_PL_UI_001 Load Plant List Page
    When Admin navigates to the Plant List page
    Then the plant table should be displayed
    And columns such as "Name", "Category", "Price", "Stock" and Actions should be visible
    And pagination controls should be displayed

  @Plants
  Scenario: Search Plants by Name
    When Admin navigates to the Plant List page
    And Admin searches for plant "Rose"
    Then the result list should contain "Rose"

  @Plants
  Scenario: Filter Plants by Category
    When Admin navigates to the Plant List page
    And Admin filters by category "Rose"
    Then all results should belong to category "Rose"

  @Plants
  Scenario: Sort Plants by Price
    When Admin navigates to the Plant List page
    And Admin sorts by "Price"
    # Then verification step to be refined based on sort order requirements if needed
