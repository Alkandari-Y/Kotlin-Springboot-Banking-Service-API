Feature: User Registration

  Scenario: successful user registration and KYC creation
    When user makes a "POST" request to "/users/v1/register" with body
      """json
      {
        "username": "testuser",
        "password": "somethingSecret1"
      }
      """
    Then the response status code should be 200
    And the response body should have a valid token with the username "testuser"
    And the user exists in the database with username of "testuser" with role "USER"
    And the users password of "somethingSecret1" is hashed and can be matched
    When user creates a kyc "POST" Request to "/users/v1/kyc" with body
    """json
    {
        "firstName": "python",
        "lastName": "Django",
        "dateOfBirth": "19-01-2007",
        "salary": 120.000,
        "nationality": "Kuwaiti"
    }
    """
    Then the kyc create response status should be 201
    And the response object should match the payload with a field "id"
    And should match the kyc request with id 1
    And the user kyc exists in the database with user id 1
    Then user makes a "GET" request with jwt token to "/users/v1/kyc"
    And status code with of 200
    And response body should match
    """json
    {
        "id": 1,
        "userId": 1,
        "firstName": "python",
        "lastName": "Django",
        "dateOfBirth": "19-01-2007",
        "salary": 120.000,
        "nationality": "Kuwaiti"
    }
    """

  Scenario: Update existing KYC with new valid data
    Given user is registered with username "updateuser" and password "ValidPass1"
    And user has a KYC with salary 50000
    """json
      {
        "firstName": "Updated",
        "lastName": "User",
        "dateOfBirth": "01-01-1990",
        "salary": 50000.00,
        "nationality": "Kuwaiti"
      }
    """
    And user has a valid jwt token to access kyc
    When user updates KYC with a "POST" request to "/users/v1/kyc" with body
    """json
    {
      "firstName": "Updated",
      "lastName": "User",
      "dateOfBirth": "01-01-1990",
      "salary": 75000.00,
      "nationality": "Kuwaiti"
    }
    """
    Then the update kyc response status should be 200
    And the response data should match the following json
    """json
      {
        "firstName": "Updated",
        "lastName": "User",
        "dateOfBirth": "01-01-1990",
        "salary": 75000.00,
        "nationality": "Kuwaiti"
      }
    """
    And the database should have updated KYC information


  Scenario: Create or update KYC with negative salary should fail
    Given user is registered with username "negativesalary" and password "ValidPass1"
    When user creates a KYC with salary -5000.00
    Then the response status should be 400
    And the response should contain error "Salary must be positive"

  Scenario: Each user should have exactly one KYC
    Given user is registered with username "onekycuser" and password "ValidPass1"
    When user creates a KYC
    And user tries to create a second KYC
    Then the response status should be 409
    And the response should contain error "User already has KYC"

  Scenario: KYC cannot be created for users under 18 years old
    Given user is registered with username "underageuser" and password "ValidPass1"
    When user creates a KYC with date of birth "01-01-2010"
    Then the response status should be 400
    And the response should contain error "User must be at least 18 years old"

  Scenario: Login fails when username does not exist
    When user attempts to login with username "nonexistent" and password "AnyPassword1"
    Then the response status code should be 401
    And the response should contain error "Invalid username or password"

  Scenario: Login fails when password is incorrect
    Given user is registered with username "validuser" and password "CorrectPassword1"
    When user attempts to login with username "validuser" and password "WrongPassword1"
    Then the response status code should be 401
    And the response should contain error "Invalid username or password"

  Scenario: Login fails when both username and password do not exist
    When user attempts to login with username "fakeuser" and password "FakePassword1"
    Then the response status code should be 401
    And the response should contain error "Invalid username or password"

  Scenario: Registration fails when username is too short
    When user tries to register with username "a" and password "ValidPassword1"
    Then the response status should be 400
    And the response should contain error "Username too short"

  Scenario: Registration fails when username already exists
    Given user is registered with username "existinguser" and password "ValidPassword1"
    When user tries to register again with username "existinguser" and password "AnotherValid1"
    Then the response status should be 409
    And the response should contain error "Username already exists"

  Scenario: Registration fails when password does not match the required pattern
    When user tries to register with username "validbutweak" and password "simple"
    Then the response status should be 400
    And the response should contain error "Password is too simple"


