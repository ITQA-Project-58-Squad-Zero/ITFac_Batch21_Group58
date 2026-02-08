@ui
Feature: Dashboard UI - Overview and Navigation
  As an authenticated user
  I want to view the dashboard after login
  So that I can see an overview of the system

  @dashboard @login_as_admin @DASH_UI_001
  Scenario: DASH_UI_001 Load Dashboard After Login
    Then Admin should see the Dashboard page
    And the Dashboard should display the Categories card
    And the Dashboard should display the Plants card
    And the Dashboard should display the Sales card
    And the Dashboard should display the Inventory card

  @dashboard @login_as_admin @DASH_UI_002
  Scenario: DASH_UI_002 Verify Dashboard Navigation Highlight
    Then Admin should see the Dashboard page
    And the Dashboard menu item should be highlighted as active

  @dashboard @login_as_admin @DASH_UI_003
  Scenario: DASH_UI_003 Display Categories Summary
    Then Admin should see the Dashboard page
    And the Categories card should display a valid Main categories count
    And the Categories card should display a valid Sub categories count

  @dashboard @login_as_admin @DASH_UI_004
  Scenario: DASH_UI_004 Display Plants Summary
    Then Admin should see the Dashboard page
    And the Plants card should display a valid Total plants count
    And the Plants card should display a valid Low Stock plants count

  @dashboard @login_as_admin @DASH_UI_005
  Scenario: DASH_UI_005 Display Sales Summary
    Then Admin should see the Dashboard page
    And the Sales card should display valid Revenue
    And the Sales card should display valid Sales count

  @dashboard @login_as_user @DASH_UI_006
  Scenario: DASH_UI_006 Dashboard Visible for User Role
    Then User should see the Dashboard page
    And the Dashboard should display the Categories card
    And the Dashboard should display the Plants card
    And the Dashboard should display the Sales card

  @dashboard @login_as_admin @DASH_UI_007
  Scenario: DASH_UI_007 Navigate to Categories Page
    Then Admin should see the Dashboard page
    When Admin clicks Categories from navigation menu
    Then the Categories page should be displayed

  @dashboard @login_as_admin @DASH_UI_008
  Scenario: DASH_UI_008 Navigate to Plants Page
    Then Admin should see the Dashboard page
    When Admin clicks Plants from navigation menu
    Then the Plants page should be displayed

  @dashboard @login_as_admin @DASH_UI_010
  Scenario: DASH_UI_010 Navigate to Inventory Page (See DASH_UI_010)
    Then Admin should see the Dashboard page
    Then the Inventory link should be disabled with tooltip "Inventory page coming soon"
