package com.coded.bankingproject.cucumber.steps

import com.coded.bankingproject.cucumber.testcontext.TestContext
import com.coded.bankingproject.cucumber.testutils.ApiRequestHelper
import com.coded.bankingproject.domain.entities.Role
import com.coded.bankingproject.domain.entities.UserEntity
import com.coded.bankingproject.repositories.UserRepository
import com.coded.bankingproject.services.JwtService
import io.cucumber.java.en.And
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*

class CommonSteps @Autowired constructor(
    private val apiRequestHelper: ApiRequestHelper,
    private val testContext: TestContext,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {

    @Given("user {string} with password {string} exists")
    fun userWithPasswordExists(username: String, password: String) {
        testContext.expectedUserName = username

        val existingUser = userRepository.findByUsername(username)
        val user = existingUser ?: userRepository.save(
            UserEntity(
                username = username,
                password = passwordEncoder.encode(password)
            )
        )

        testContext.token = jwtService.generateToken(user)
    }


    @When("user sends a {string} request to {string} with body:")
    fun userSendsRequest(method: String, endpoint: String, payload: String) {
        testContext.requestBody = payload
        testContext.response = apiRequestHelper.sendRequest(
            method = method,
            endpoint = endpoint,
            payload = payload,
            responseType = String::class.java
        )
        extractTokenIfPresent()
    }

    @When("user sends a {string} request to {string} with token and body:")
    fun userSendsRequestWithToken(method: String, endpoint: String, payload: String) {
        testContext.requestBody = payload
        testContext.response = apiRequestHelper.sendRequest(
            method = method,
            endpoint = endpoint,
            payload = payload,
            jwtToken = testContext.token,
            responseType = String::class.java
        )
    }

    @When("user sends a {string} request to {string} with token")
    fun userSendsRequestWithTokenNoBody(method: String, endpoint: String) {

        testContext.response = apiRequestHelper.sendRequest(
            method = method,
            endpoint = endpoint.replace("{accountNumber}", testContext.accountNumber),
            jwtToken = testContext.token,
            responseType = String::class.java
        )
    }

    @When("user attempts to login with username {string} and password {string}")
    fun userAttemptsLogin(username: String, password: String) {
        val jsonObject = JSONObject()
        jsonObject.put("username", username)
        jsonObject.put("password", password)
        testContext.response = apiRequestHelper.sendRequest(
            method = "POST",
            endpoint = "/users/v1/login",
            payload = jsonObject.toString(),
            responseType = String::class.java
        )
    }

    @Then("the response status code is {int}")
    fun responseStatusCodeIs(expectedStatus: Int) {
        assertEquals(expectedStatus, testContext.response?.statusCode?.value())
    }

    @And("the response contains a valid JWT token with username {string}")
    fun responseContainsValidJwtToken(username: String) {
        assertNotNull(testContext.response?.body)
        val json = JSONObject(testContext.response?.body)
        val token = json.getString("token")
        val extractedUsername = jwtService.extractUsername(token)
        assertEquals(username, extractedUsername)
    }

    @And("the database has a user {string} with role {string}")
    fun databaseHasUserWithRole(username: String, role: String) {
        val user = userRepository.findByUsername(username)
        assertNotNull(user)
        assertEquals(username, user?.username)
        assertEquals(Role.valueOf(role), user?.role)
    }

    @And("the error message is {string}")
    fun errorMessageIs(expectedError: String) {
        val json = JSONObject(testContext.response?.body)
        assertTrue(json.has("message"))
        assertEquals(expectedError, json.getString("message"))
    }

    @And("the field {string} has validation error {string}")
    fun fieldHasValidationError(fieldName: String, expectedMessage: String) {
        val json = JSONObject(testContext.response?.body)
        assertTrue(json.has("fieldErrors"), "Response does not contain 'fieldErrors'")

        val fieldErrors = json.getJSONArray("fieldErrors")

        val matchingError = (0 until fieldErrors.length())
            .map { fieldErrors.getJSONObject(it) }
            .firstOrNull { it.getString("field") == fieldName }

        assertNotNull(matchingError, "No validation error found for field: $fieldName")
        assertEquals(expectedMessage, matchingError!!.getString("message"))
    }

    private fun extractTokenIfPresent() {
        testContext.response?.body?.let {
            val json = JSONObject(it)
            if (json.has("token")) {
                testContext.token = json.getString("token")
            }
        }
    }

    private fun generateTokenForUser(username: String): String {
        val user = userRepository.findByUsername(username)
        return jwtService.generateToken(user!!)
    }
}
