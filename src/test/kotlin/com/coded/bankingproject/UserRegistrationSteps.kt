package com.coded.bankingproject

import io.cucumber.java.en.And
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.util.MultiValueMap

class UserRegistrationSteps {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    private var response: ResponseEntity<String>? = null
    private var token = ""
    private var headersMap = mutableMapOf<String, List<String>>()

    @When("user makes a {string} request to {string} with body")
    fun `user makes a request to`(method: String, endpoint: String, payload: String) {
        println(payload)
        val requestMethod = HttpMethod.valueOf(method)
        val headers = HttpHeaders(
            MultiValueMap.fromMultiValue(headersMap)
        )
        val requestEntity = HttpEntity(payload, headers)
        response = testRestTemplate.exchange(
            endpoint,
            requestMethod,
            requestEntity,
            String::class.java
        )

    }

    @Then("the response status code should be {int}")
    fun theRegisterResponseStatusCode(expectedStatusCode: Int) {
        assertEquals(expectedStatusCode, response?.statusCode?.value())
    }

    @And("the response body should have a valid token")
    fun theRegisterResponseBodyHasAToken(expectedBody: String) {
        assertEquals(expectedBody, response?.body)
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