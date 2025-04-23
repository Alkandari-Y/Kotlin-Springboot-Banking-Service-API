Feature: Hello World

  Scenario: The happy path where I get hello world
    When I make a GET request to "/accounts/v1/accounts"
    Then the response status code should be 200
    And the response body should be "Hello World Welcome to my server"