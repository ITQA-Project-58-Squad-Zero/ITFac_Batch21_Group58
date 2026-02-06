@Plants @User @login_as_user
Feature: Plants User Actions
  As a Standard User
  I want to view plant information
  But I should not have access to administrative actions

  @Plants @User @AddPlant
  Scenario: TC_PL_UI_U_001 Hide Add Plant Button (User)
    When User navigates to the Plant List page
    Then the "Add a Plant" button should not be visible

  @Plants @User @EditDeleteAction
  Scenario: TC_PL_UI_U_002 Restrict Edit/Delete Actions (User)
    When User navigates to the Plant List page
    Then the current user should be identified as "User"
    And the Edit action should not be visible for any plant
    And the Delete action should not be visible for any plant
