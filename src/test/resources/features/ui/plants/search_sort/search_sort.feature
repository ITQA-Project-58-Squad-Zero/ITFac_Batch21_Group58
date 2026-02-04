@Plants @login_as_admin
Feature: Plants Search and Sort Functionality

  @Plants
  Scenario: TC_PL_UI_001 Load Plant List Page
    When Admin navigates to the Plant List page
    Then the plant table should be displayed
    And columns such as "Name", "Category", "Price", "Stock" and Actions should be visible
    And pagination controls should be displayed

  @Plants @Pagination
  Scenario: TC_PL_UI_002 Display Paginated Plant List
    When Admin navigates to the Plant List page
    Then the plant table should be displayed
    And pagination controls should be visible
    And the Previous button should be disabled
    And the admin should be on page 1
    When Admin records the current plant names
    And Admin clicks the Next page button
    Then the admin should be on page 2
    And the plant records should change
    And the Previous button should be enabled
    When Admin clicks the Previous page button
    Then the admin should be on page 1
    And the Previous button should be disabled

  @Plants @Search
  Scenario: TC_PL_UI_003 Search Plants by Name
    Given at least one plant exists in the system
    When Admin navigates to the Plant List page
    And Admin enters "Rose" in the search field
    And Admin clicks the Search button
    Then only plants matching "Rose" should be displayed
    And all displayed plant names should contain "Rose"

  @Plants @Search @MultiWord @KnownBug
  Scenario: TC_PL_UI_003b Search Plants by Multi-Word Name (Known Bug)
    Given a plant named "Red Rose" exists in the system
    When Admin navigates to the Plant List page
    And Admin enters "Red Rose" in the search field
    And Admin clicks the Search button
    Then only plants matching "Red Rose" should be displayed
    And the plant "Red Rose" should be in the results

  @Plants @Filter
  Scenario: TC_PL_UI_004 Filter Plants by Category
    When Admin navigates to the Plant List page
    And Admin selects "Rose" from the category dropdown
    And Admin clicks the Search button
    Then only plants belonging to category "Rose" should be displayed
    And all displayed plants should have category "Rose"

  @Plants @Sort
  Scenario: TC_PL_UI_005 Sort Plants by Name
    When Admin navigates to the Plant List page
    Then plants should be sorted by "Name" in "ascending" order
    When Admin clicks the "Name" column header
    Then plants should be sorted by "Name" in "descending" order
    When Admin clicks the "Name" column header again
    Then plants should be sorted by "Name" in "ascending" order

  @Plants @Sort
  Scenario: TC_PL_UI_006 Sort Plants by Price
    When Admin navigates to the Plant List page
    And Admin clicks the "Price" column header
    Then plants should be sorted by "Price" in "ascending" order
    When Admin clicks the "Price" column header again
    Then plants should be sorted by "Price" in "descending" order

  @Plants @Sort
  Scenario: TC_PL_UI_007 Sort Plants by Quantity
    When Admin navigates to the Plant List page
    And Admin clicks the "Stock" column header
    Then plants should be sorted by "Stock" in "ascending" order
    When Admin clicks the "Stock" column header again
    Then plants should be sorted by "Stock" in "descending" order

  @Plants @Badge
  Scenario: TC_PL_UI_008 Display Low Stock Badge
    When Admin navigates to the Plant List page
    Then plants with quantity below 5 should display a "Low" badge

  @Plants @EmptyState
  Scenario: TC_PL_UI_009 Display No Plants Message
    When Admin navigates to the Plant List page
    And Admin enters "NonExistentPlant" in the search box
    And Admin clicks the "Search" button
    Then a "No plants found" message should be displayed
