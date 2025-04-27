package com.coded.bankingproject.cucumber.steps

import com.coded.bankingproject.cucumber.testcontext.TestContext
import com.coded.bankingproject.cucumber.testutils.ApiRequestHelper
import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.repositories.KYCRepository
import com.coded.bankingproject.repositories.UserRepository
import com.coded.bankingproject.repositories.kycDateFormatter
import io.cucumber.java.en.When
import io.cucumber.java.en.And
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal
import java.time.LocalDate

class UserKYCSteps {
    @Autowired
    lateinit var apiRequestHelper: ApiRequestHelper

    @Autowired
    lateinit var testContext: TestContext

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var kycRepository: KYCRepository

    private var expectedKYCEntity: KYCEntity? = null

    @When("user tries to create another KYC:")
    fun userTriesToCreateAnotherKYC(body: String) {
        testContext.response = apiRequestHelper.sendRequest(
            method = "POST",
            endpoint = "/users/v1/kyc",
            payload = body,
            jwtToken = testContext.token,
            responseType = String::class.java
        )
    }

    @And("the stored password for {string} matches {string}")
    fun storedPasswordMatches(username: String, password: String) {
        val user = userRepository.findByUsername(username)
        assertNotNull(user)
        assertTrue(passwordEncoder.matches(password, user?.password))
    }

    @And("the response contains a KYC object matching the request with a field {string}")
    fun responseContainsKycObject(field: String) {
        val jsonResponse = JSONObject(testContext.response?.body)
        assertTrue(jsonResponse.has(field))
        val requestJson = JSONObject(testContext.requestBody)
        requestJson.put(field, jsonResponse.get(field))
        requestJson.put("userId", jsonResponse.get("userId"))
        JSONAssert.assertEquals(requestJson, jsonResponse, JSONCompareMode.LENIENT)
    }

    @And("the database has a KYC linked to user {string}")
    fun databaseHasKycForUser(username: String) {
        val user = userRepository.findByUsername(username)
        assertNotNull(user)
        val kyc = kycRepository.findKYCByUserId(user?.id!!)
        assertNotNull(kyc)
    }

    @And("the response matches the user's KYC information")
    fun responseMatchesUserKyc() {
        val requestJson = JSONObject(testContext.requestBody)
        val responseJson = JSONObject(testContext.response?.body)

        requestJson.put("id", responseJson.get("id"))
        requestJson.put("userId", responseJson.get("userId"))

        JSONAssert.assertEquals(requestJson, responseJson, JSONCompareMode.STRICT)
    }

    @And("user has a KYC:")
    fun userHasAKyc(payload: String) {
        val user = userRepository.findByUsername(testContext.expectedUserName)
        val json = JSONObject(payload)

        expectedKYCEntity = kycRepository.save(
            KYCEntity(
                id = null,
                user = user,
                firstName = json.getString("firstName"),
                lastName = json.getString("lastName"),
                dateOfBirth = LocalDate.parse(json.getString("dateOfBirth"), kycDateFormatter),
                salary = BigDecimal.valueOf(json.getDouble("salary")),
                nationality = json.getString("nationality")
            )
        )
    }

    @And("the updated KYC information is saved in the database")
    fun updatedKycInformationSaved() {
        val stored = kycRepository.findByIdOrNull(expectedKYCEntity?.id!!)
        val json = JSONObject(testContext.response?.body)

        assertEquals(stored?.id, json.getLong("id"))
        assertEquals(stored?.user?.id, json.getLong("userId"))
        assertEquals(stored?.firstName, json.getString("firstName"))
        assertEquals(stored?.lastName, json.getString("lastName"))
        assertEquals(stored?.nationality, json.getString("nationality"))
        assertEquals(
            0,
            stored?.salary?.compareTo(BigDecimal.valueOf(json.getDouble("salary")))
        )
    }

    @And("the database should could one instance of kyc")
    fun useHasOneKycDatabaseRecord() {
        val userEntity = userRepository.findByUsername(testContext.expectedUserName)
        val kycList = kycRepository.findAllByUserId(userEntity?.id!!)
        assertEquals(1, kycList.size)
    }
}
