Feature: Category Management API
  As an Admin
  I want to manage categories
  So that I can organize products effectively

  @admin_auth
  Scenario: CM2_API_A_01 Create Category via API Successfully
    Given the admin has a valid session
    When I create a category with name "abcdee"
    Then the response status code should be 201
    And the response body should return correct category details with name "abcdee"
    And the new category should be present in the categories list

  @admin_auth
  Scenario: CM2_API_A_02 Reject Category Creation without Name
    Given the admin has a valid session
    When I create a category with missing name
    Then the response status code should be 400
    And the response body should contain a validation error for missing name

  @admin_auth
  Scenario: CM2_API_A_03 Update Category via API
    Given the admin has a valid session
    And a category exists with name "OldCat"
    When I update the category name to "NewCat"
    Then the response status code should be 200
    And the response body should return correct category details with name "NewCat"

  @admin_auth
  Scenario: CM2_API_A_04 Delete Category via API
    Given the admin has a valid session
    And a category exists with name "DelCat"
    When I delete the category
    Then the response status code should be 204
    And the category should no longer exist

  @admin_auth
  Scenario: CM2_API_A_05 Reject Duplicate Category Creation via API
    Given the admin has a valid session
    And a category exists with name "DupCat"
    When I create a category with name "DupCat"
    Then the response status code should be 400
    And the response body should contain a duplicate resource error
    And no new duplicate category should be created
