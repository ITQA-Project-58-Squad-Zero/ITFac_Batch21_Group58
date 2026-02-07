@ui @Plants @Admin @login_as_admin
Feature: Plant Management UI - Admin Actions
  As an Admin
  I want to have exclusive access to administrative actions
  So that I can manage the plant inventory

  @Plants @Admin @AddPlant @PM1_UI_A_001
  Scenario: PM1_UI_A_00 1 View Add Plant Button (Admin)
    When Admin navigates to the Plant List page
    Then the "Add a Plant" button should be visible

  @Plants @Admin @EditAction @PM1_UI_A_002
  Scenario: PM1_UI_A_00 2 View Edit Action (Admin)
    When Admin navigates to the Plant List page
    Then the Edit action should be visible for every plant

  @Plants @Admin @DeleteAction @PM1_UI_A_003
  Scenario: PM1_UI_A_00 3 View Delete Action (Admin)
    When Admin navigates to the Plant List page
    Then the Delete action should be visible for every plant
