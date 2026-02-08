@api @plants_api
Feature: Plant Management API
  As an Admin
  I want to manage plants via API
  So that I can create, retrieve, and manage plant data

  Background:
    Given the admin has a valid session

  @admin_auth @PM1_API_001
  Scenario: PM1_API_001 Get Paginated Plant List
    When I request all plants
    Then the response status code should be 200
    And the response body should contain a list of plants

  @admin_auth @PM1_API_002
  Scenario: PM1_API_002 Search Plants by Name (API)
    Given a first available plant exists in the system
    When I search for plants with the first available plant name
    Then the response status code should be 200
    And all returned plants should have names containing the searched term

  @admin_auth @PM1_API_003
  Scenario: PM1_API_003 Filter Plants by Category (API)
    Given a first available category exists in the system
    When I filter plants by the first available category ID
    Then the response status code should be 200
    And all returned plants should belong to the filtered category

  @admin_auth @PM1_API_004
  Scenario: PM1_API_004 Sort Plants by Name (API)
    When I request plants sorted by "name" in "asc" order
    Then the response status code should be 200
    And the plants should be sorted by name in ascending order

  @admin_auth @PM1_API_005
  Scenario: PM1_API_005 Sort Plants by Price (API)
    When I request plants sorted by "price" in "desc" order
    Then the response status code should be 200
    And the plants should be sorted by price in descending order

  @admin_auth @PM1_API_006
  Scenario: PM1_API_006 Sort Plants by Quantity (API)
    When I request plants sorted by "quantity" in "asc" order
    Then the response status code should be 200
    And the plants should be sorted by quantity in ascending order
  
  
  

  @admin_auth @PM2_API_A_01
  Scenario: PM2_API_A_01 Add New Plant (Valid Data) (admin)
    
    
    
    Given Valid sub-category exists
    When Send POST request with valid Name, Price, Quantity to /api/plants/category/{validCategoryId}
    Then Verify status code is 201
    And API returns success and plant is created

  @admin_auth @PM2_API_A_02
  Scenario: PM2_API_A_02 Add Plant with Missing Name (admin)
    
    
    Given Valid sub-category exists
    When Send POST request with JSON body missing Name
    Then Verify status code is 400
    And API returns validation error and plant is not created

  @admin_auth @PM2_API_A_03
  Scenario: PM2_API_A_03 Add Plant with Invalid Category (admin)
    
    
    
    
    Given Valid sub-category exists
    When Send POST request with category that is not a sub-category
    Then Verify status code is 400 or 404
    And API returns validation error and plant is not created

  @admin_auth @PM2_API_A_04
  Scenario: PM2_API_A_04 Add Plant with Price Invalid (admin)
    
    
    Given Valid sub-category exists
    When Send POST request with Price = 0 or negative
    Then Verify status code is 400
    And API returns validation error and plant is not created
  
  
  

  @admin_auth @PM2_API_A_05
  Scenario: PM2_API_A_05 Edit Plant (admin)
    
    
    Given Valid plant ID exists
    When Send PUT request to /api/plants/{validId} with updated Name, Price, Quantity
    Then Verify status code is 200
    And API returns 200 OK
  
  
  

  @user_auth @PM2_API_U_01
  Scenario: PM2_API_U_01 Deny Plant Creation via API (Non-Admin User)
    
    
    Given the user has a valid session
    And Valid sub-category exists
    When User sends POST request to /api/plants/category/{categoryId} with valid data
    Then Verify status code is 403
    And API blocks plant creation for non-admin user

  @user_auth @PM2_API_U_02
  Scenario: PM2_API_U_02 Deny Plant Update via API (Non-Admin User)
    
    
    Given the user has a valid session
    And Valid plant ID exists for verification
    When User sends PUT request to /api/plants/{id} with updated data
    Then Verify status code is 403
    And Verify plant data remains unchanged

  @user_auth @PM2_API_U_03
  Scenario: PM2_API_U_03 Deny Plant Deletion via API (Non-Admin User)
    
    
    Given the user has a valid session
    And Valid plant ID exists for verification
    When User sends DELETE request to /api/plants/{id}
    Then Verify status code is 403
    And Verify plant still exists in the system

  @user_auth @PM2_API_U_04
  Scenario: PM2_API_U_04 Deny Plant Creation under Sub-Category via API (Non-Admin User)
    
    
    Given the user has a valid session
    And Valid sub-category exists
    When User sends POST request to /api/plants/category/{categoryId} with valid plant data
    Then Verify status code is 403
    And Plant is not created in the system

  @no_auth @PM2_API_U_05
  Scenario: PM2_API_U_05 Deny Plant Update/Delete via API without Authorization Token
    
    
    Given Valid plant ID exists for verification
    When Send PUT request without authentication token
    Then Verify status code is 401
    And No data is modified
