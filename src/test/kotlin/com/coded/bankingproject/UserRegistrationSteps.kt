package com.coded.bankingproject

import com.coded.bankingproject.repositories.KYCRepository
import com.coded.bankingproject.repositories.UserRepository
import com.coded.bankingproject.services.JwtService
import io.cucumber.java.en.And
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.util.MultiValueMap
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode

class UserRegistrationSteps {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var kycRepository: KYCRepository


    private var expectedUserName: String = ""
    private var expectedPassword: String = ""

    private var token: String = ""
    private var response: ResponseEntity<String>? = null
    private var headersMap = mutableMapOf<String, List<String>>()

    @When("user makes a {string} request to {string} with body")
    fun `user makes a request to`(method: String, endpoint: String, payload: String) {
        val requestMethod = HttpMethod.valueOf(method)
        headersMap["Content-Type"] = listOf("application/json")
        val headers = HttpHeaders(
            MultiValueMap.fromMultiValue(headersMap)
        )
        val jsonRequest = JSONObject(payload)

        expectedUserName = jsonRequest.getString("username")
        expectedPassword = jsonRequest.getString("password")

        val requestEntity = HttpEntity(payload, headers)
        response = testRestTemplate.exchange(
            endpoint,
            requestMethod,
            requestEntity,
            String::class.java
        )
        assertNotNull(response?.body)
    }

    @Then("the response status code should be {int}")
    fun theRegisterResponseStatusCode(expectedStatusCode: Int) {
        assertEquals(expectedStatusCode, response?.statusCode?.value())
    }

    @And("the response body should have a valid token")
    fun theRegisterResponseBodyHasAToken() {
        println(token)
        val jwtUsername = jwtService.extractUsername(token)
        println(jwtUsername)
        println(expectedUserName)
        assertEquals(expectedUserName, jwtUsername)
    }

    @When("user creates a kyc POST Request")
    fun userCanCreateKycPostRequest() {

    }

    @Then("the response status should be {int}")
    fun kycResponseStatusCode(statusCode: Int) {

    }

    @And("the response body should have the kyc request object with the field {string}")
    fun kycResponseHasIdField() {

    }


}