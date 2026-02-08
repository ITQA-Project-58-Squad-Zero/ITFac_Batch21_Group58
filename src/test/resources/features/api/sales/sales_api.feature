@api
Feature: Sales Management API
  As an Admin
  I want to manage sales
  So that I can track sales details

  @admin_auth @SM_API_A_09
  Scenario: SM_API_A_09 Get Sale by ID Successfully (Admin)
    Given the admin has a valid session
    And an existing sale exists in the system
    When I request the sale details by the stored sale ID
    Then the response status code should be 200
    And the response body should return correct sale details for the stored sale

  @admin_auth @SM_API_A_11
  Scenario: SM_API_A_11 Get All Sales Successfully
    Given the admin has a valid session
    When I request all sales
    Then the response status code should be 200
    And the response body should contain a list of sales

  @admin_auth @SM_API_A_06
  Scenario: SM_API_A_06 Create Sale via API Successfully (Admin)
    Given the admin has a valid session
    And a first available plant exists in the system
    When I create a sale for the first available plant with quantity 1
    Then the response status code should be 201
    And the response body should return correct sale details
    And the new sale should be present in the sales list

  @admin_auth @SM_API_A_07
  Scenario: SM_API_A_07 Reject API Sale Creation for Quantity = 0 (Admin)
    Given the admin has a valid session
    And a first available plant exists in the system
    And I have the current sales count
    When I create a sale for the first available plant with quantity 0
    Then the response status code should be 400
    And the response body should contain a validation message
    And the sales count should remain unchanged

  @admin_auth @SM_API_A_08
  Scenario: SM_API_A_08 Reject API Sale Creation for Insufficient Stock (Admin)
    Given the admin has a valid session
    And a first available plant exists in the system
    And I have the current sales count
    When I create a sale for the first available plant with quantity 1000000
    Then the response status code should be 400
    And the response body should contain an insufficient stock error
    And the sales count should remain unchanged

  @user_auth @SM_API_U_01
  Scenario: SM_API_U_01 Retrieve All Sales via API (Non-Admin User)
    Given the user has a valid session
    When I request all sales
    Then the response status code should be 200
    And the response body should contain a list of sales

  @user_auth @SM_API_U_02
  Scenario: SM_API_U_02 Get Sale by ID Successfully via API (Non-Admin User)
    Given the user has a valid session
    And an existing sale exists in the system
    When I request the sale details by the stored sale ID
    Then the response status code should be 200
    And the response body should return correct sale details for the stored sale

  @user_auth @SM_API_U_03
  Scenario: SM_API_U_03 Deny Sale Creation via API (Forbidden) (Non-Admin User)
    Given the user has a valid session
    And a first available plant exists in the system
    When I create a sale for the first available plant with quantity 1
    Then the response status code should be 403
    And the response body should indicate forbidden access

  @user_auth @SM_API_U_04
  Scenario: SM_API_U_04 Deny Sale Deletion via API (Forbidden) (Non-Admin User)
    Given the user has a valid session
    And an existing sale exists in the system
    When I attempt to delete the stored sale
    Then the response status code should be 403
    And the response body should indicate forbidden access
    When I request the sale details by the stored sale ID
    Then the response status code should be 200

  @user_auth @SM_API_U_05
  Scenario: SM_API_U_05 Retrieve Paginated Sales via API (Non-Admin User)
    Given the user has a valid session
    When I request paginated sales with page 0 and size 5
    Then the response status code should be 200
    And the response should contain at most 5 sales records

  @admin_auth @SM_API_A_10
  Scenario: SM_API_A_10 Delete Sale via API Successfully (Admin)
    Given the admin has a valid session
    And a first available plant exists in the system
    And I create a sale for the first available plant with quantity 1
    And the response status code should be 201
    When I delete the sale by ID from the response
    Then the response status code should be 200 or 204
    When I request the sale details by the deleted sale ID
    Then the response status code should be 404
