Feature: Plant Management API
  As an Admin
  I want to manage plants
  So that I can maintain the plant inventory

  @admin_auth
  Scenario: PM2_API_A_01 Add New Plant (Valid Data)
    Given the admin has a valid session
    And Valid sub-category exists
    When Send POST request with valid Name, Price, Quantity to valid category 3
    Then Verify status code is 201
    And API returns success and plant is created

  @admin_auth
  Scenario: PM2_API_A_02 Add Plant with Missing Name
    Given the admin has a valid session
    And Valid sub-category exists
    When Send POST request with JSON body missing Name
    Then Verify status code is 400
    And API returns validation error and plant is not created

  @admin_auth
  Scenario: PM2_API_A_03 Add Plant with Invalid Category
    Given the admin has a valid session
    And Valid sub-category exists
    When Send POST request with category that is not a sub-category
    Then Verify status code is 400
    And API returns validation error and plant is not created

  @admin_auth
  Scenario: PM2_API_A_04 Add Plant with Price Invalid
    Given the admin has a valid session
    And Valid sub-category exists
    When Send POST request with Price = 0 or negative
    Then Verify HTTP status code is 400
    And API returns 400 Bad Request

  @admin_auth
  Scenario: PM2_API_A_05 Edit Plant
    Given the admin has a valid session
    And Valid plant ID exists
    When Send PUT request to /api/plants/{validId} with updated Name, Price, Quantity
    Then Verify HTTP status code is 200
    And API returns 200 OK

  @admin_auth
  Scenario: PM2_API_U_02 Deny Plant Update (Non-Admin)
    Given the admin has a valid session
    And Valid plant ID exists
    And I switch to non-admin user
    When Send PUT request to /api/plants/{validId} with updated Name, Price, Quantity
    Then Verify status code is 403 Forbidden
    And Verify plant data is unchanged

  @admin_auth
  Scenario: PM2_API_U_03 Deny Plant Deletion (Non-Admin)
    Given the admin has a valid session
    And Valid plant ID exists
    And I switch to non-admin user
    When Send DELETE request to /api/plants/{id}
    Then Verify status code is 403 Forbidden
    And Verify plant still exists

  @user_auth
  Scenario: PM2_API_U_01 Deny Plant Creation (Non-Admin)
    Given the user has a valid session
    And Valid sub-category exists
    When Send POST request with valid Name, Price, Quantity to valid category 3
    Then Verify status code is 403 Forbidden
    And Verify plant is not created

  @user_auth
  Scenario: PM2_API_U_04 Deny Plant Creation under SubCategory (Non-Admin)
    Given the user has a valid session
    And Valid sub-category exists
    When Send POST request with valid Name, Price, Quantity to valid category 3
    Then Verify status code is 403 Forbidden

  @admin_auth
  Scenario: PM2_API_U_05 Deny Request without Token
    Given the admin has a valid session
    And Valid plant ID exists
    And I clear the session token
    When Send PUT request to /api/plants/{id}
    Then Verify status code is 401 Unauthorized
    When Send DELETE request to /api/plants/{id} no auth
    Then Verify status code is 401 Unauthorized
