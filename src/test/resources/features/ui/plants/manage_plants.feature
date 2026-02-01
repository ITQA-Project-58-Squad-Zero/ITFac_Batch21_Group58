Feature: Plant Management
  As an Admin
  I want to manage plants
  So that I can keep track of plant inventory

  @plants @login_as_admin
  Scenario: PM2_UI_A_01 Verify Add Plant page access for admin
    When Admin navigates to the "Plants" page
    And Admin clicks the "Add a Plant" button
    Then the "Add Plant" page should be displayed successfully
