@plants_api
Feature: API - Plants Management
  As an Admin
  I want to manage plants via API
  So that I can create, retrieve, and manage plant data

  Background:
    Given the admin has a valid session

  @admin_auth @TC_PL_API_001
  Scenario: TC_PL_API_001 Get All Plants Successfully
    When I request all plants
    Then the response status code should be 200
    And the response body should contain a list of plants

  @admin_auth @TC_PL_API_002
  Scenario: TC_PL_API_002 Search Plants by Name
    Given a first available plant exists in the system
    When I search for plants with the first available plant name
    Then the response status code should be 200
    And all returned plants should have names containing the searched term

  @admin_auth @TC_PL_API_003
  Scenario: TC_PL_API_003 Filter Plants by Category
    Given a first available category exists in the system
    When I filter plants by the first available category ID
    Then the response status code should be 200
    And all returned plants should belong to the filtered category

  @admin_auth @TC_PL_API_004
  Scenario: TC_PL_API_004 Sort Plants by Name
    When I request plants sorted by "name" in "asc" order
    Then the response status code should be 200
    And the plants should be sorted by name in ascending order

  @admin_auth @TC_PL_API_005
  Scenario: TC_PL_API_005 Sort Plants by Price
    When I request plants sorted by "price" in "desc" order
    Then the response status code should be 200
    And the plants should be sorted by price in descending order

  @admin_auth @TC_PL_API_006
  Scenario: TC_PL_API_006 Sort Plants by Quantity
    When I request plants sorted by "quantity" in "asc" order
    Then the response status code should be 200
    And the plants should be sorted by quantity in ascending order
  # ============================================================================
  # CREATE OPERATIONS - Add New Plants
  # ============================================================================

  @admin_auth @PM2_API_A_01
  Scenario: PM2_API_A_01 Add New Plant with Valid Data
    # Test Case: Verify a new plant can be added under a sub-category
    # using POST /api/plants/category/{categoryId}
    # Preconditions: Admin token/session is available, Valid sub-category exists
    Given Valid sub-category exists
    When Send POST request with valid Name, Price, Quantity to /api/plants/category/{validCategoryId}
    Then Verify status code is 201
    And API returns success and plant is created

  @admin_auth @PM2_API_A_02
  Scenario: PM2_API_A_02 Add Plant with Missing Name
    # Test Case: Verify API blocks plant creation when name is missing
    # Preconditions: Admin token/session is available, Valid sub-category exists
    Given Valid sub-category exists
    When Send POST request with JSON body missing Name
    Then Verify status code is 400
    And API returns validation error and plant is not created

  @admin_auth @PM2_API_A_03
  Scenario: PM2_API_A_03 Add Plant with Invalid Category
    # Test Case: Verify API blocks plant creation when using an invalid/non-existent category ID
    # Preconditions: Admin token/session is available, Valid sub-category exists
    # Expected: API should reject invalid category IDs and return 400 or 404
    #           Only valid sub-category IDs should be accepted for plant creation
    Given Valid sub-category exists
    When Send POST request with category that is not a sub-category
    Then Verify status code is 400 or 404
    And API returns validation error and plant is not created

  @admin_auth @PM2_API_A_04
  Scenario: PM2_API_A_04 Add Plant with Invalid Price
    # Test Case: Verify API blocks plant creation when price is 0 or negative
    # Preconditions: Admin token/session is available, Valid sub-category exists
    Given Valid sub-category exists
    When Send POST request with Price = 0 or negative
    Then Verify status code is 400
    And API returns validation error and plant is not created
  # ============================================================================
  # UPDATE OPERATIONS - Edit Existing Plants
  # ============================================================================

  @admin_auth @PM2_API_A_05
  Scenario: PM2_API_A_05 Edit Plant with Valid Data
    # Test Case: Verify an existing plant can be updated with valid data
    # Preconditions: Admin token/session is available, Valid plant ID exists
    Given Valid plant ID exists
    When Send PUT request to /api/plants/{validId} with updated Name, Price, Quantity
    Then Verify status code is 200
    And API returns 200 OK
  # ============================================================================
  # AUTHORIZATION TESTS - Non-Admin User (PM2_API_U_01 to PM2_API_U_05)
  # ============================================================================

  @user_auth @PM2_API_U_01
  Scenario: PM2_API_U_01 Deny Plant Creation via API (Non-Admin User)
    # Test Case: Verify Non-Admin user cannot create plants using POST /api/plants
    # Preconditions: User token/session is available (Non-Admin), Valid categoryId exists
    Given the user has a valid session
    And Valid sub-category exists
    When User sends POST request to /api/plants/category/{categoryId} with valid data
    Then Verify status code is 403
    And API blocks plant creation for non-admin user

  @user_auth @PM2_API_U_02
  Scenario: PM2_API_U_02 Deny Plant Update via API (Non-Admin User)
    # Test Case: Verify Non-Admin user cannot update plants using PUT /api/plants/{id}
    # Preconditions: User token/session is available (Non-Admin), Valid plant ID exists
    Given the user has a valid session
    And Valid plant ID exists for verification
    When User sends PUT request to /api/plants/{id} with updated data
    Then Verify status code is 403
    And Verify plant data remains unchanged

  @user_auth @PM2_API_U_03
  Scenario: PM2_API_U_03 Deny Plant Deletion via API (Non-Admin User)
    # Test Case: Verify Non-Admin user cannot delete plants using DELETE /api/plants/{id}
    # Preconditions: User token/session is available (Non-Admin), Valid plant ID exists
    Given the user has a valid session
    And Valid plant ID exists for verification
    When User sends DELETE request to /api/plants/{id}
    Then Verify status code is 403
    And Verify plant still exists in the system

  @user_auth @PM2_API_U_04
  Scenario: PM2_API_U_04 Deny Plant Creation under Sub-Category via API (Non-Admin User)
    # Test Case: Verify Non-Admin user cannot create plants under sub-category
    # Preconditions: Valid categoryId exists, User is logged in as non-admin
    Given the user has a valid session
    And Valid sub-category exists
    When User sends POST request to /api/plants/category/{categoryId} with valid plant data
    Then Verify status code is 403
    And Plant is not created in the system

  @no_auth @PM2_API_U_05
  Scenario: PM2_API_U_05 Deny Plant Update/Delete via API without Authorization Token
    # Test Case: Verify requests without authentication token are rejected
    # Preconditions: Valid plant ID exists, No token provided in the request
    Given Valid plant ID exists for verification
    When Send PUT request without authentication token
    Then Verify status code is 401
    And No data is modified
