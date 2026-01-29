Feature: Admin Login

  Scenario: Admin logs in successfully
    Given Admin is on the login page
    When Admin enters username "admin" and password "admin123"
    Then Admin should see the dashboard

  Scenario: Login with invalid credentials
    Given Admin is on the login page
    When Admin enters username "admin" and password "wrongness"
    Then Admin should see error message "Invalid username or password."

    