package com.coded.bankingproject.cucumber.steps

import com.coded.bankingproject.cucumber.testcontext.TestContext
import com.coded.bankingproject.cucumber.testutils.ApiRequestHelper
import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.entities.UserEntity
import com.coded.bankingproject.repositories.AccountRepository
import com.coded.bankingproject.repositories.TransactionRepository
import com.coded.bankingproject.repositories.UserRepository
import io.cucumber.java.en.And
import io.cucumber.java.en.When
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal
import kotlin.test.assertEquals

class AccountTransferSteps {
    @Autowired
    lateinit var apiRequestHelper: ApiRequestHelper

    @Autowired
    lateinit var testContext: TestContext

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var userRepository: UserRepository

    private var userInitiatingTransaction: UserEntity? = null
    private var sourceAccount: AccountEntity? = null
    private var destinationAccount: AccountEntity? = null


    @When("user makes a transfer of {double}")
    fun userMakesATransferOfAmount(amount: Double) {
        testContext.requestBody = JSONObject()
            .put("sourceAccountNumber", sourceAccount!!.accountNumber)
            .put("destinationAccountNumber", destinationAccount!!.accountNumber)
            .put("amount", amount)
            .toString()

        testContext.response = apiRequestHelper.sendRequest(
            method = "POST",
            endpoint = "/accounts/v1/accounts/transfer",
            payload = testContext.requestBody,
            jwtToken = testContext.token,
            responseType = String::class.java
        )
    }

    @When("user makes a transfer of {double} to same account")
    fun userTriesToCloseAccount(amount: Double) {
        sourceAccount = accountRepository.findByAccountNumber(testContext.accountNumber)!!
        destinationAccount = sourceAccount!!.copy()

        testContext.requestBody = JSONObject()
            .put("sourceAccountNumber", sourceAccount!!.accountNumber)
            .put("destinationAccountNumber", sourceAccount!!.accountNumber)
            .put("amount", amount)
            .toString()

        testContext.response = apiRequestHelper.sendRequest(
            method = "POST",
            endpoint = "/accounts/v1/accounts/transfer",
            payload = testContext.requestBody,
            jwtToken = testContext.token,
            responseType = String::class.java
        )
    }

    @And("{string} has an active source account {string} with balance {double}")
    fun userHasActiveSourceAccountWithBalance(username: String, accountName: String, balance: Double) {
        userInitiatingTransaction = userRepository.findByUsername(username)!!
        val account = AccountEntity(
            name = accountName,
            balance = BigDecimal.valueOf(balance),
            user = userInitiatingTransaction,
            isActive = true
        )
        val savedAccount = accountRepository.save(account)
        sourceAccount = savedAccount
    }

    @And("{string} has an active destination account {string} with balance {double}")
    fun userHasActiveDestinationAccountWithBalance(username: String, accountName: String, balance: Double) {
        val user = userRepository.findByUsername(username)!!
        val account = AccountEntity(
            name = accountName,
            balance = BigDecimal.valueOf(balance).setScale(3),
            user = user,
            isActive = true
        )
        val savedAccount = accountRepository.save(account)
        destinationAccount = savedAccount
    }

    @And("user has an active account named {string} with balance {double}")
    fun userHasActiveAccountWithBalance(name: String, balance: Double) {
        val username = testContext.expectedUserName
        val user = userRepository.findByUsername(username)!!
        val account = AccountEntity(
            name = name,
            balance = BigDecimal.valueOf(balance),
            user = user,
            isActive = true
        )
        val newAccount = accountRepository.save(account)
        testContext.accountNumber = newAccount.accountNumber
    }

    @And("the database has an active account for user {string} named {string} with balance {double}")
    fun databaseHasActiveAccountForUser(username: String, accountName: String, balance: Double) {
        val balanceBigDecimal = BigDecimal.valueOf(balance)
        val newAccountId = JSONObject(testContext.response?.body)
            .getLong("id")
        val newAccount = accountRepository.findByIdOrNull(newAccountId)
        assertNotNull(newAccount)
        assertEquals(accountName, newAccount?.name)
        assertEquals(0, newAccount?.balance?.compareTo(balanceBigDecimal))
        assertEquals(username, newAccount?.user?.username)
        assertTrue(newAccount?.isActive!!)
    }

    @And("user has {int} active accounts")
    fun userHasActiveAccounts(count: Int) {
        val username = testContext.expectedUserName
        val user = userRepository.findByUsername(username)!!
        for (i in 1..count) {
            val accountName = "Account $i"
            val account = AccountEntity(
                name = accountName,
                balance = BigDecimal.valueOf(500.0),
                user = user,
                isActive = true
            )
            accountRepository.save(account)
        }
    }

    @And("the response contains the user's active accounts")
    fun responseContainsUsersActiveAccounts() {
        assertNotNull(testContext.response?.body)
        val jsonArray = JSONArray(testContext.response?.body)
        assertTrue(jsonArray.length() > 0)
    }

    @And("the source account balance decreases by {string}")
    fun sourceAccountBalanceDecreases(amount: String) {
        val amountTransferred = BigDecimal(amount).setScale(3)

        val jsonRequestBodyAmount = JSONObject(testContext.requestBody).getDouble("amount")
        assertNotNull(jsonRequestBodyAmount)
        val requestAmount = BigDecimal.valueOf(jsonRequestBodyAmount).setScale(3)

        assertEquals(0, amountTransferred.compareTo(requestAmount))

        val jsonResponse = JSONObject(testContext.response?.body)
        assertTrue(jsonResponse.has("newBalance"))
        val jsonResponseNewBalance = jsonResponse.getDouble("newBalance")
        assertNotNull(jsonResponseNewBalance)
        val newBalanceResponse = BigDecimal.valueOf(jsonResponseNewBalance).setScale(3)

        val updatedAccountEntity = accountRepository.findByAccountNumber(sourceAccount?.accountNumber!!)!!
        assertNotNull(updatedAccountEntity)

        val expectedNewBalance = sourceAccount?.balance?.subtract(amountTransferred)


        assertEquals(0, expectedNewBalance?.compareTo(updatedAccountEntity.balance))
        assertEquals(0, expectedNewBalance?.compareTo(newBalanceResponse))
        assertEquals(0, updatedAccountEntity.balance.compareTo(newBalanceResponse))
    }


    @And("the destination account balance increases by {string}")
    fun destinationAccountBalanceIncreases(amount: String) {
        val jsonResponse = JSONObject(testContext.response?.body)
        assertTrue(jsonResponse.has("newBalance"))

        val updatedDestinationAccountEntity = accountRepository.findByAccountNumber(destinationAccount?.accountNumber!!)!!
        assertNotNull(updatedDestinationAccountEntity)

        val balanceFromDatabase = updatedDestinationAccountEntity.balance.setScale(3)
        val initialBalance = destinationAccount?.balance?.setScale(3)

        val amountTransferred = BigDecimal(amount).setScale(3)

        val expectedNewBalance = initialBalance?.add(amountTransferred)

        assertEquals(0, expectedNewBalance?.compareTo(balanceFromDatabase))
    }


    @And("the account {string} is marked as inactive in the database")
    fun accountIsMarkedAsInactive(accountName: String) {
        val account = accountRepository.findByAccountNumber(testContext.accountNumber)
        assertNotNull(account)
        assertEquals(accountName, account?.name)
        assertFalse(account!!.isActive)
    }

    @And("the account {string} is marked as active in the database")
    fun accountIsMarkedAsActive(accountName: String) {
        val account = accountRepository.findByAccountNumber(testContext.accountNumber)
        assertNotNull(account)
        assertEquals(accountName, account?.name)
        assertTrue(account!!.isActive)
    }

    @And("the database does not contain the failed transaction")
    fun databaseDoesNotContainFailedTransactions() {
        val transactions = transactionRepository.findBySourceAccountId(sourceAccount?.id!!)
        assertNotNull(transactions)
        assertEquals(0, transactions.count())
    }

    @And("the database has a transaction for the source account id and destination account id with {double} amount")
    fun databaseHasSuccessfulTransaction(amount: Double) {
        val amountBigDecimal = BigDecimal.valueOf(amount).setScale(3)
        val transactions = transactionRepository.findBySourceAccountId(sourceAccount?.id!!)
        assertNotNull(transactions)
        assertEquals(1, transactions.count())
        val transaction = transactions.first()
        assertNotNull(transaction)
        assertEquals(0, amountBigDecimal.compareTo(transaction.amount))
        assertEquals(sourceAccount?.id, transaction.sourceAccount?.id)
        assertEquals(destinationAccount?.id, transaction.destinationAccount?.id)
    }
}
