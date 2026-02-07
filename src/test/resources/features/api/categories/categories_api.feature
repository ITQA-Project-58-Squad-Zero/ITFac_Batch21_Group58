Feature: Categories API
  As an Admin or User
  I want to access category data via API
  So that I can verify category endpoints and data

  @admin_auth @CM_API_A_01
  Scenario: TC001 Get All Categories
    Given the admin has a valid session
    When I request all categories with no name or parentId filter
    Then the response status code should be 200
    And the response body should be a list of categories

  @admin_auth @CM_API_A_02
  Scenario: TC002 Get Category by ID (Success)
    Given the admin has a valid session
    And a stored category exists in the system
    When I request the category by the stored category ID
    Then the response status code should be 200
    And the response body should return the category with the stored ID

  @admin_auth @CM_API_A_03
  Scenario: TC003 Get Main Categories Only
    Given the admin has a valid session
    When I request main categories
    Then the response status code should be 200
    And each returned category should have no parent

  @admin_auth @CM_API_A_12
  Scenario: TC012 Get Category Summary
    Given the admin has a valid session
    When I request the categories summary
    Then the response status code should be 200
    And the response body should contain mainCategories and subCategories
    And mainCategories and subCategories should be numeric and greater than or equal to zero
