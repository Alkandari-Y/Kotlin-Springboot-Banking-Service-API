Feature: User Registration

  Scenario: As user I can register and create add a kyc

    When user makes a "POST" request to "/users/v1/register" with body
      """json
      {
        "username": "admin",
        "password": "someta1hingSecret"
      }
      """

    Then the response status code should be 200
    And the response body should have a valid token
    When user creates a kyc POST Request
    Then the response status should be 201
    And the response body should have the kyc request object with the field "id"