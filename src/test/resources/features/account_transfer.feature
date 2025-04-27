Feature: Account Management and Money Transfers

  Scenario: User creates an account with valid initial balance
    Given user "validuser" with password "ValidPass1" exists
    When user sends a "POST" request to "/accounts/v1/accounts" with token and body:
      """json
      {
        "initialBalance": 500.00,
        "name": "Savings Account"
      }
      """
    Then the response status code is 201
    And the database has an active account for user "validuser" named "Savings Account" with balance 500.00

  Scenario: User closes their own active account successfully
    Given user "closeruser" with password "ValidPass1" exists
    And user has an active account named "Closing Account" with balance 300.00
    When user sends a "POST" request to "/accounts/v1/accounts/{accountNumber}/close" with token
    Then the response status code is 200
    And the account "Closing Account" is marked as inactive in the database

  Scenario: User fails to create account with balance below minimum
    Given user "lowbalanceuser" with password "ValidPass1" exists
    When user sends a "POST" request to "/accounts/v1/accounts" with token and body:
      """json
      {
        "initialBalance": 50.00,
        "name": "Checking Account"
      }
      """
    Then the response status code is 400
    And the field "initialBalance" has validation error "Initial balance must be at least 100.00"

  Scenario: User cannot have more than three active accounts
    Given user "limiteduser" with password "ValidPass1" exists
    And user has 3 active accounts
    When user sends a "POST" request to "/accounts/v1/accounts" with token and body:
      """json
      {
        "initialBalance": 500.00,
        "name": "Overflow Account"
      }
      """
    Then the response status code is 400
    And the error message is "Account limit reached. Please visit your nearest NBK branch."

  Scenario: User can retrieve their accounts
    Given user "accountviewer" with password "ValidPass1" exists
    And user has an active account named "Main Account" with balance 1000.00
    When user sends a "GET" request to "/accounts/v1/accounts" with token
    Then the response status code is 200
    And the response contains the user's active accounts

  Scenario: Successful money transfer between two accounts
    Given user "transferuser" with password "ValidPass1" exists
    And "transferuser" has an active source account "transferuser Source Account" with balance 500.00
    And "transferuser" has an active destination account "transferuser Destination Account" with balance 100.00
    When user makes a transfer of 200.50
    Then the response status code is 200
    And the source account balance decreases by 200.50
    And the destination account balance increases by 200.50

  Scenario: Cannot transfer to the same account
    Given user "selftransferuser" with password "ValidPass1" exists
    And "selftransferuser" has an active source account "owneruser Account" with balance 500.00
    When user makes a transfer of 50 to same account
    Then the response status code is 400
    And the error message is "Cannot transfer to the same account."

  Scenario: Cannot transfer with insufficient funds
    Given user "owneruser" with password "ValidPass1" exists
    And "owneruser" has an active destination account "Big Wallet" with balance 1000.00
    Given user "pooruser" with password "ValidPass1" exists
    And "pooruser" has an active source account "Empty Wallet" with balance 50.00
    When user makes a transfer of 100
    Then the response status code is 400
    And the error message is "Transfer would result in a negative balance."

  Scenario: Cannot transfer from an account the user does not own
    Given user "victims" with password "ValidPass1" exists
    And "victims" has an active source account "Crypto Wallet" with balance 20000.00
    Given user "hacker" with password "ValidPass1" exists
    And "hacker" has an active destination account "Empty Wallet" with balance 50.00
    When user makes a transfer of 10000
    Then the response status code is 400
    And the error message is "Cannot transfer with another persons account."

  Scenario: User cannot close someone else's account
    Given user "victimuser" with password "ValidPass1" exists
    And user has an active account named "Victim Account" with balance 300.00
    Given user "attacker" with password "ValidPass2" exists
    When user sends a "POST" request to "/accounts/v1/accounts/{accountNumber}/close" with token
    Then the response status code is 400
    And the error message is "Only owners can close accounts"
    And the account "Victim Account" is marked as active in the database
    And the error message is "Only owners can close accounts"

  Scenario: Cannot transfer between negative amount
    Given user "unsuspectinguser" with password "ValidPass1" exists
    And "unsuspectinguser" has an active destination account "unsuspectinguser Destination Account" with balance 10000.00
    Given user "sneakyUser" with password "ValidPass1" exists
    And "sneakyUser" has an active source account "sneakyUser Source Account" with balance 500.00
    When user makes a transfer of -9000.00
    Then the response status code is 400
    And the error message is "Validation failed"
    And the field "amount" has validation error "amount must must be at least 1.00"


