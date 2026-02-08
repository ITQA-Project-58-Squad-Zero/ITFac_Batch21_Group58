@api
Feature: Category Management API - Write Operations
  As an Admin
  I want to manage categories
  So that I can organize products effectively

  @admin_auth @CM2_API_A_01
  Scenario: CM2_API_A_01 Create Category via API Successfully
    Given the admin has a valid session
    When I create a category with name "OkCat"
    Then the response status code should be 201
    And the response body should return correct category details
    And the new category should be present in the categories list

  @admin_auth @CM2_API_A_02
  Scenario: CM2_API_A_02 Reject Category Creation without Name
    Given the admin has a valid session
    When I create a category with missing name
    Then the response status code should be 400
    And the response body should contain a validation error for missing name

  @admin_auth @CM2_API_A_03
  Scenario: CM2_API_A_03 Update Category via API
    Given the admin has a valid session
    And a category exists with name "OldCat"
    When I update the category name to "NewCat"
    Then the response status code should be 200
    And the response body should return correct category details with name "NewCat"

  @admin_auth @CM2_API_A_04
  Scenario: CM2_API_A_04 Delete Category via API
    Given the admin has a valid session
    And a category exists with name "DelAdmin"
    When I delete the category with name "DelAdmin"
    Then the response status code should be 204
    And the category "DelAdmin" should no longer exist

  @admin_auth @CM2_API_A_05
  Scenario: CM2_API_A_05 Reject Duplicate Category Creation via API
    Given the admin has a valid session
    And a category exists with name "DupCat"
    When I create a category with exact name "DupCat"
    Then the response status code should be 400
    And the response body should contain a duplicate resource error
    And no new duplicate category should be created

  @user_auth @CM2_API_U_01
  Scenario: CM2_API_U_01 Reject Add Category API Request (User)
    Given the user has a valid session
    When I create a category with name "UserCat"
    Then the response status code should be 403
    And the response should indicate insufficient permissions

  @user_auth @CM2_API_U_02
  Scenario: CM2_API_U_02 Enforce Category Management Access Control (User)
    Given the user has a valid session
    And the admin has created a category with name "OldCatUsr"
    When I create a category with name "UserCat"
    Then the response status code should be 403
    When I update the category name to "UserUpd"
    Then the response status code should be 403
    When I delete the category with name "OldCatUsr"
    Then the response status code should be 403

  @user_auth @CM2_API_U_03
  Scenario: CM2_API_U_03 Reject Delete Category API Request (User)
    Given the user has a valid session
    And the admin has created a category with name "DelUser"
    When I delete the category with name "DelUser"
    Then the response status code should be 403
    And the response should indicate insufficient permissions

  @user_auth @CM2_API_U_04
  Scenario: CM2_API_U_04 Reject Edit Category via API (User)
    Given the user has a valid session
    And the admin has created a category with name "OldUser"
    When I update the category name to "UserEdt"
    Then the response status code should be 403
    And the response should indicate insufficient permissions

  @user_auth @CM2_API_U_05
  Scenario: CM2_API_U_05 Reject Add Sub-Category via API (User)
    Given the admin has a valid session
    And the admin has created a category with name "ParentCat"
    Given the user has a valid session
    When I create a sub-category with name "SubCat" under parent "ParentCat"
    Then the response status code should be 403
    And the response should indicate insufficient permissions
