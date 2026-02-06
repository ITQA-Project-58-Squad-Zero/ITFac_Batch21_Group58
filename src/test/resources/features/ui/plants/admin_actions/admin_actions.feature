@Plants @Admin @login_as_admin
Feature: Plants Admin Actions
  As an Admin
  I want to have exclusive access to administrative actions
  So that I can manage the plant inventory

  @Plants @Admin @AddPlant
  Scenario: TC_PL_UI_A_001 View Add Plant Button (Admin)
    When Admin navigates to the Plant List page
    Then the "Add a Plant" button should be visible

  @Plants @Admin @EditAction
  Scenario: TC_PL_UI_A_002 View Edit Action (Admin)
    When Admin navigates to the Plant List page
    Then the Edit action should be visible for every plant

  @Plants @Admin @DeleteAction
  Scenario: TC_PL_UI_A_003 View Delete Action (Admin)
    When Admin navigates to the Plant List page
    Then the Delete action should be visible for every plant
