Feature: Create post

  Scenario: User creates a post successfully
    Given user is registered and logged in
    When user creates a new post
    Then new post should be visible in posts list