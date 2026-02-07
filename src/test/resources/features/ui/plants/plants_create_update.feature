Feature: Plant Management UI - Create and Update
  As an Admin
  I want to manage plants
  So that I can keep track of plant inventory

  @plants @login_as_admin @PM2_UI_A_01
  Scenario: PM2_UI_A_01 Verify Add Plant page access for admin
    When Admin navigates to the "Plants" page
    And Admin clicks the "Add a Plant" button
    Then the "Add Plant" page should be displayed successfully

  @plants @login_as_admin @PM2_UI_A_02
  Scenario: PM2_UI_A_02 Verify Plant Name maximum length validation(admin)
    When Admin navigates to the "Plants" page
    And Admin clicks the "Add a Plant" button
    Then the "Add Plant" page should be displayed successfully
    When Admin enters a "Plant Name" with more than 25 characters
    And Admin fills remaining fields with valid data
    And Admin clicks the "Save" button
    Then the error message "Plant name must be between 3 and 25 characters" should be displayed

  @plants @login_as_admin @PM2_UI_A_03
  Scenario: PM2_UI_A_03 Verify Plant Name minimum length validation(admin)
    When Admin navigates to the "Plants" page
    And Admin clicks the "Add a Plant" button
    Then the "Add Plant" page should be displayed successfully
    When Admin enters a "Plant Name" with less than 3 characters
    And Admin fills remaining fields with valid data
    And Admin clicks the "Save" button
    Then the error message "Plant name must be between 3 and 25 characters" should be displayed

  @plants @login_as_admin @PM2_UI_A_04
  Scenario: PM2_UI_A_04 Verify mandatory field validation on Add Plant(admin)
    When Admin navigates to the "Plants" page
    And Admin clicks the "Add a Plant" button
    Then the "Add Plant" page should be displayed successfully
    When Admin leaves all mandatory fields empty
    And Admin clicks the "Save" button
    Then the following error messages should be displayed
      | Plant name is required |
      | Category is required   |
      | Price is required      |
      | Quantity is required   |

  @plants @login_as_admin @PM2_UI_A_05
  Scenario: PM2_UI_A_05 Verify Price greater than zero (admin)
    When Admin navigates to the "Plants" page
    And Admin clicks the "Add a Plant" button
    Then the "Add Plant" page should be displayed successfully
    When Admin enters a valid Plant Name
    And Admin selects a valid Category
    And Admin enters a Price of "-10"
    And Admin enters a valid Quantity
    And Admin clicks the "Save" button
    Then the error message "Price must be greater than 0" should be displayed

  @plants @login_as_user @PM2_UI_U_01
  Scenario: PM2_UI_U_01 Verify non-admin user cannot see Add a Plant button(non admin user)
    When User navigates to the "Plants" page
    Then the "Add a Plant" button should not be visible

  @plants @login_as_user @PM2_UI_U_02
  Scenario: PM2_UI_U_02 Verify non-admin user cannot see edit button(non admin user)
    When User navigates to the "Plants" page
    Then the "Edit" button should not be visible

  @plants @login_as_user @PM2_UI_U_03
  Scenario: PM2_UI_U_03 Verify non-admin user cannot see delete button(non admin user)
    When User navigates to the "Plants" page
    Then the "Delete" button should not be visible

  @plants @login_as_user @PM2_UI_U_04
  Scenario: PM2_UI_U_04 Verify non-admin user cannot access Add Plant page
    When User navigates to the "Add Plant" page directly via URL
    Then the "Add Plant" page should not be displayed

  @plants @login_as_user @PM2_UI_U_05
  Scenario: PM2_UI_U_05 Verify non-admin user cannot access Edit Plant page
    Given a first available plant exists
    When User navigates to the "Edit Plant" page with the first available plant ID directly via URL
    Then the "Edit Plant" page should not be displayed
