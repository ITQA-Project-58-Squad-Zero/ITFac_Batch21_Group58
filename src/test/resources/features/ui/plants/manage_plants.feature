Feature: Plant Management
  As an Admin
  I want to manage plants
  So that I can keep track of plant inventory

  @plants @login_as_admin
  Scenario: PM2_UI_A_01 Verify Add Plant page access for admin
    When Admin navigates to the "Plants" page
    And Admin clicks the "Add a Plant" button
    Then the "Add Plant" page should be displayed successfully

  @plants @login_as_admin
  Scenario: PM2_UI_A_02 Verify Plant Name maximum length validation(admin)
    When Admin navigates to the "Plants" page
    And Admin clicks the "Add a Plant" button
    Then the "Add Plant" page should be displayed successfully
    When Admin enters a "Plant Name" with more than 25 characters
    And Admin fills remaining fields with valid data
    And Admin clicks the "Save" button
    Then the error message "Plant name must be between 3 and 25 characters" should be displayed
