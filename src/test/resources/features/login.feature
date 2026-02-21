Feature: User login

  Scenario: Successful login
    Given user is registered
    And user is on login page
    When user enters valid credentials
    And user submits login form
    Then user should be redirected to posts page