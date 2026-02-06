Feature: Sales Management API
  As an Admin
  I want to manage sales
  So that I can track sales details

  @admin_auth
  Scenario: SM_API_A_09 Get Sale by ID Successfully
    Given the admin has a valid session
    And a sale exists with ID 21
    When I request the sale details by ID 21
    Then the response status code should be 200
    And the response body should return correct sale details having id 21

  @admin_auth
  Scenario: SM_API_A_11 Get All Sales Successfully
    Given the admin has a valid session
    When I request all sales
    Then the response status code should be 200
    And the response body should contain a list of sales
    And the response body should contain a list of sales

  @admin_auth
  Scenario: SM_API_A_06 Create Sale via API Successfully
    Given the admin has a valid session
    When I create a sale for plant ID 1 with quantity 1
    Then the response status code should be 201
    And the response body should return correct sale details
    And the new sale should be present in the sales list

  @admin_auth
  Scenario: SM_API_A_07 Reject API Sale Creation for Quantity = 0
    Given the admin has a valid session
    And I have the current sales count
    When I create a sale for plant ID 1 with quantity 0
    Then the response status code should be 400
    And the response body should contain a validation message
    And the sales count should remain unchanged

  @admin_auth
  Scenario: SM_API_A_08 Reject API Sale Creation for Insufficient Stock
    Given the admin has a valid session
    And I have the current sales count
    When I create a sale for plant ID 1 with quantity 1000000
    Then the response status code should be 400
    And the response body should contain an insufficient stock error
    And the sales count should remain unchanged

  @user_auth
  Scenario: SM_API_U_01 Retrieve All Sales via API (Non-Admin User)
    Given the user has a valid session
    When I request all sales
    Then the response status code should be 200
    And the response body should contain a list of sales
