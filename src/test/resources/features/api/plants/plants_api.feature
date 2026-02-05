Feature: Plants Management API
  As an Admin
  I want to manage plants
  So that I can retrieve and verify plant data

  @admin_auth @plants_api
  Scenario: TC_PL_API_001 Get All Plants Successfully
    Given the admin has a valid session
    When I request all plants
    Then the response status code should be 200
    And the response body should contain a list of plants

  @admin_auth @plants_api
  Scenario: TC_PL_API_002 Search Plants by Name
    Given the admin has a valid session
    When I search for plants with name "Rose"
    Then the response status code should be 200
    And all returned plants should have names containing "Rose"

  @admin_auth @plants_api
  Scenario: TC_PL_API_003 Filter Plants by Category
    Given the admin has a valid session
    When I filter plants by category ID 1
    Then the response status code should be 200
    And all returned plants should belong to category ID 1

  @admin_auth @plants_api
  Scenario: TC_PL_API_004 Sort Plants by Name
    Given the admin has a valid session
    When I request plants sorted by "name" in "asc" order
    Then the response status code should be 200
    And the plants should be sorted by name in ascending order

  @admin_auth @plants_api
  Scenario: TC_PL_API_005 Sort Plants by Price
    Given the admin has a valid session
    When I request plants sorted by "price" in "desc" order
    Then the response status code should be 200
    And the plants should be sorted by price in descending order

  @admin_auth @plants_api
  Scenario: TC_PL_API_006 Sort Plants by Quantity
    Given the admin has a valid session
    When I request plants sorted by "quantity" in "asc" order
    Then the response status code should be 200
    And the plants should be sorted by quantity in ascending order
