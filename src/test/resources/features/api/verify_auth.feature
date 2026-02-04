Feature: Verify API Authentication

  @api_auth
  Scenario: Authentication Hook Validation
    Given the API authentication hook has run
    Then a valid token should be stored
