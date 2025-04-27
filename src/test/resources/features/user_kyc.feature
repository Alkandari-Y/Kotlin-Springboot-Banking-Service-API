Feature: User and KYC

  Scenario: Successful user registration and KYC creation
    When user sends a "POST" request to "/users/v1/register" with body:
      """json
      {
        "username": "testuser",
        "password": "somethingSecret1"
      }
      """
    Then the response status code is 200
    And the response contains a valid JWT token with username "testuser"
    And the database has a user "testuser" with role "USER"
    And the stored password for "testuser" matches "somethingSecret1"
    When user sends a "POST" request to "/users/v1/kyc" with token and body:
      """json
      {
        "firstName": "python",
        "lastName": "Django",
        "dateOfBirth": "19-01-2007",
        "salary": 120000.00,
        "nationality": "Kuwaiti"
      }
      """
    Then the response status code is 201
    And the response contains a KYC object matching the request with a field "id"
    And the database has a KYC linked to user "testuser"
    When user sends a "GET" request to "/users/v1/kyc" with token
    Then the response status code is 200
    And the response matches the user's KYC information

  Scenario: Update existing KYC with new valid data
    Given user "updateuser" with password "ValidPass1" exists
    And user has a KYC:
      """json
      {
        "firstName": "Updated",
        "lastName": "User",
        "dateOfBirth": "01-01-1990",
        "salary": 50000.00,
        "nationality": "Kuwaiti"
      }
      """
    When user sends a "POST" request to "/users/v1/kyc" with token and body:
      """json
      {
        "firstName": "Updated",
        "lastName": "User",
        "dateOfBirth": "01-01-1990",
        "salary": 75000.00,
        "nationality": "Kuwaiti"
      }
      """
    Then the response status code is 201
    And the updated KYC information is saved in the database

  Scenario: Creating KYC with negative salary fails
    Given user "negativesalary" with password "ValidPass1" exists
    When user sends a "POST" request to "/users/v1/kyc" with token and body:
      """json
      {
        "firstName": "Negative",
        "lastName": "Salary",
        "dateOfBirth": "01-01-1990",
        "salary": -5000.00,
        "nationality": "Kuwaiti"
      }
      """
    Then the response status code is 400
    And the error message is "Validation failed"
    And the field "salary" has validation error "Minimum salary cannot be below 0"

  Scenario: User cannot create multiple KYC records
    Given user "onekycuser" with password "ValidPass1" exists
    And user has a KYC:
      """json
      {
        "firstName": "First",
        "lastName": "KYC",
        "dateOfBirth": "01-01-1990",
        "salary": 50000.00,
        "nationality": "Kuwaiti"
      }
      """
    When user tries to create another KYC:
      """json
      {
        "firstName": "Second",
        "lastName": "Attempt",
        "dateOfBirth": "01-01-1990",
        "salary": 60000.00,
        "nationality": "Kuwaiti"
      }
      """
    Then the response status code is 201
    And the database should could one instance of kyc

  Scenario: KYC cannot be created for users under 18 years old
    Given user "underageuser" with password "ValidPass1" exists
    When user sends a "POST" request to "/users/v1/kyc" with token and body:
      """json
      {
        "firstName": "Young",
        "lastName": "User",
        "dateOfBirth": "01-01-2010",
        "salary": 12000.00,
        "nationality": "Kuwaiti"
      }
      """
    Then the response status code is 400
    And the error message is "User must be 18 or older"

  Scenario: Login fails when username does not exist
    When user attempts to login with username "nonexistent" and password "AnyPassword1"
    Then the response status code is 401
    And the error message is "Invalid Credentials"

  Scenario: Login fails when password is incorrect
    Given user "validuser" with password "CorrectPassword1" exists
    When user attempts to login with username "validuser" and password "WrongPassword1"
    Then the response status code is 401
    And the error message is "Invalid Credentials"

  Scenario: Login fails when both username and password do not exist
    When user attempts to login with username "fakeuser" and password "FakePassword1"
    Then the response status code is 401
    And the error message is "Invalid Credentials"

  Scenario: Registration fails when username is too short
    When user sends a "POST" request to "/users/v1/register" with body:
      """json
      {
        "username": "a",
        "password": "ValidPassword1"
      }
      """
    Then the response status code is 400
    And the error message is "Validation failed"
    And the field "username" has validation error "Username is too short"

  Scenario: Registration fails when username already exists
    Given user "existinguser" with password "ValidPassword1" exists
    When user sends a "POST" request to "/users/v1/register" with body:
      """json
      {
        "username": "existinguser",
        "password": "AnotherValid1"
      }
      """
    Then the response status code is 400
    And the error message is "Username already exists"

  Scenario: Registration fails when password is too weak
    When user sends a "POST" request to "/users/v1/register" with body:
      """json
      {
        "username": "validbutweak",
        "password": "simple"
      }
      """
    Then the response status code is 400
    And the error message is "Validation failed"
    And the field "password" has validation error "Password is too simple"

  Scenario: User cannot assign themselves ADMIN role during registration
    When user sends a "POST" request to "/users/v1/register" with body:
    """json
    {
      "username": "adminUser",
      "password": "someta1hingSecret",
      "role": "ADMIN"
    }
    """
    Then the response status code is 200
    And the response contains a valid JWT token with username "adminUser"
    And the database has a user "adminUser" with role "USER"
