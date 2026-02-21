Feature: User registration

  Scenario: Successful registration
    Given user is on register page
    When user fills valid registration form
    Then user should be redirected to login page