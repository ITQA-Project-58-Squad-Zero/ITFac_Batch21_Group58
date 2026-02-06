Feature: Dashboard API
  As an authorized user
  I want to view dashboard summary data
  So that I can see the overview of the system status

  @admin_auth @TC_DASH_API_001
  Scenario: TC_DASH_API_001 Get Category Summary Data
    Given the admin has a valid session
    When I request the categories summary
    Then the response status code should be 200
    And the response body should contain mainCategories and subCategories
    And mainCategories and subCategories should be numeric and greater than or equal to zero

  @admin_auth @TC_DASH_API_002
  Scenario: TC_DASH_API_002 Get Plant Summary Data
    Given the admin has a valid session
    When I request the plant summary
    Then the response status code should be 200
    And the response body should contain totalPlants and lowStockPlants

  @admin_auth @TC_DASH_API_003
  Scenario: TC_DASH_API_003 Get Sales Summary Data
    Given the admin has a valid session
    When I request the sales summary
    Then the response status code should be 200
    And the response body should contain revenue and sales count

  @user_auth @TC_DASH_API_004
  Scenario Outline: TC_DASH_API_004 Dashboard API Access for User Role
    Given the user has a valid session
    When I request the <summaryType> summary
    Then the response status code should be 200

    Examples:
      | summaryType |
      | categories  |
      | plant       |
      | sales       |
