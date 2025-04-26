package com.coded.bankingproject

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.domain.entities.Role
import com.coded.bankingproject.domain.entities.UserEntity
import com.coded.bankingproject.repositories.KYCRepository
import com.coded.bankingproject.repositories.UserRepository
import com.coded.bankingproject.repositories.kycDateFormatter
import com.coded.bankingproject.services.JwtService
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal
import java.time.LocalDate

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

    private var expectedKYCEntity: KYCEntity? = null
    private var expectedKYCJson: String = ""
    private var token: String = ""
    private var response: ResponseEntity<String>? = null

    @When("user makes a {string} request to {string} with body")
    fun `user makes a request to`(method: String, endpoint: String, payload: String) {
        val jsonRequest = JSONObject(payload)
        expectedUserName = jsonRequest.getString("username")
        expectedPassword = jsonRequest.getString("password")

        response = sendRequest(
            method=method,
            endpoint=endpoint,
            payload=payload
        )

        val data = JSONObject(response?.body)
        token = data.getString("token")
    }

    @Then("the response status code should be {int}")
    fun theRegisterResponseStatusCode(expectedStatusCode: Int) {
        assertEquals(expectedStatusCode, response?.statusCode?.value())
    }

    @And("the response body should have a valid token with the username {string}")
    fun theRegisterResponseBodyHasAToken(usernameInToken: String) {
        val jwtUsername = jwtService.extractUsername(token)
        assertEquals(usernameInToken, jwtUsername)
    }

    @And("the user exists in the database with username of {string} with role {string}")
    fun userExistsInDatabase(usernameExists: String, role: String) {
        val user = userRepository.findByUsername(usernameExists)
        assertNotNull(user)
        assertEquals(usernameExists, user?.username)

        val expectedRole = Role.valueOf(role)
        assertEquals(expectedRole, user?.role)
    }

    @And("the users password of {string} is hashed and can be matched")
    fun userPasswordHashedAndMatches(userPassword: String) {
        val user = userRepository.findByUsername(expectedUserName)
        assertNotNull(user)
        assert(user?.password!!.startsWith("\$2"))

        val passwordMatch = passwordEncoder.matches(expectedPassword, user.password)
        assertNotNull(passwordMatch)
        assert(passwordMatch)
        assertNotEquals(expectedPassword, user.password)

        val changedUserPassword = userPassword + "addingExtraStuff"
        assertNotEquals(expectedUserName, changedUserPassword)

        val invalidPassword = passwordEncoder.matches(changedUserPassword, user.password)
        assertEquals(false, invalidPassword)
    }

    @When("user creates a kyc {string} Request to {string} with body")
    fun userCanCreateKycPostRequest(method: String, endpoint: String, payload: String) {
        expectedKYCJson = payload

        response = sendRequest(
            method = method,
            endpoint=endpoint,
            payload=payload,
            jwtToken=token
        )
    }

    @Then("the kyc create response status should be {int}")
    fun kycResponseStatusCode(statusCode: Int) {
        assertNotNull(response?.statusCode)
        assertEquals(201, response?.statusCode?.value())
    }

    @Then("the response object should match the payload with a field {string}")
    fun testKycResponseWithField(jsonField: String) {
        val data = JSONObject(response?.body)
        assertNotNull(data)

        val hasIdField = data.has("id")
        assertTrue(hasIdField)
    }

    @And("should match the kyc request with id {int}")
    fun kycRequestMatchesResponse(expectedKycId: Int) {
        val expectedKycJson = JSONObject(expectedKYCJson)
        expectedKycJson.put("id", 1)

        val testData = JSONObject(response?.body)
        assertNotNull(testData)
        JSONAssert.assertEquals(expectedKycJson, testData, JSONCompareMode.LENIENT)
    }

    @And("the user kyc exists in the database with user id {int}")
    fun useKycExistsInDatabase(userId: Long) {
        val storedKyc = kycRepository.findKYCByUserId(userId)
        assertNotNull(storedKyc)
        assertEquals(userId, storedKyc?.userId)
    }

    @Then("user makes a {string} request with jwt token to {string}")
    fun userMakesRequestWithJwtToken(method: String, endpoint: String) {
        response = sendRequest(
            method = method,
            endpoint=endpoint,
            jwtToken=token
        )
    }

    @And("status code with of {int}")
    fun statusCodeWithOfOfOk(statusCode: Int) {
        assertNotNull(response?.statusCode)
        assertEquals(statusCode, response?.statusCode?.value())
    }

    @And("response body should match")
    fun kycGetRequestWithBody(payload: String) {
        JSONAssert.assertEquals(expectedKYCJson, response?.body, JSONCompareMode.LENIENT)
    }

    @Given("user is registered with username {string} and password {string}")
    fun userRegisteredWithUsername(username: String, password: String) {
        expectedUserName = username
        expectedPassword = password

        val userEntity = UserEntity(
            username= username,
            password = passwordEncoder.encode(password)
        )
        val user = userRepository.save(userEntity)
        token = jwtService.generateToken(userEntity)

        assertNotNull(user)
        assertEquals(username, user.username)
        assertTrue(passwordEncoder.matches(expectedPassword, user.password))
    }

    @And("user has a KYC with salary {double}")
    fun userHasAKYCWithSalary(salary: Double, kycToSave: String) {
        val salaryBigDecimal = BigDecimal.valueOf(salary)

        val user = userRepository.findByUsername(expectedUserName)
        assertNotNull(user)
        assertNotNull(user?.id)

        val kycJson = JSONObject(kycToSave)
        val jsonSalaryBigDecimal = BigDecimal.valueOf(kycJson.getDouble("salary"))
        assertEquals(salaryBigDecimal, jsonSalaryBigDecimal)

        expectedKYCEntity = kycRepository.save(
            KYCEntity(
                id= null,
                user = user,
                firstName = kycJson.getString("firstName"),
                lastName = kycJson.getString("lastName"),
                dateOfBirth = LocalDate.parse(kycJson.getString("dateOfBirth"), kycDateFormatter),
                salary = jsonSalaryBigDecimal,
                nationality = kycJson.getString("nationality"),
            )
        )

        assertNotNull(expectedKYCEntity)
        assertNotNull(expectedKYCEntity?.id)
        assertNotNull(expectedKYCEntity?.dateOfBirth)
    }

    @And("user has a valid jwt token to access kyc")
    fun userHasValidJwtTokenForKycGetRequest() {
        val userEntity = userRepository.findByUsername(expectedUserName)
        assertNotNull(userEntity)

        token = jwtService.generateToken(userEntity!!)
        assertNotNull(token)
        val tokenUserName = jwtService.extractUsername(token)
        assertNotNull(tokenUserName)
        assertEquals(expectedUserName, tokenUserName)
    }

    @When("user updates KYC with a {string} request to {string} with body")
    fun updateKycRequest(method: String, endpoint: String, payload: String) {
        response = sendRequest(
            method = method,
            endpoint=endpoint,
            payload=payload,
            jwtToken=token
        )
        println(payload)

    }

    @Then("the update kyc response status should be {int}")
    fun updateKycResponseStatusCode(statusCode: Int) {
        assertNotNull(response?.statusCode)
        assertEquals(201, response?.statusCode?.value())
    }

    @And("the response data should match the following json")
    fun updateKycResponseJsonMatches(expectedPayload: String) {
        expectedKYCJson = response?.body ?: ""

        assertNotNull(expectedKYCJson)

        val expectedJson = JSONObject(expectedPayload)

        expectedJson.put("id", expectedKYCEntity?.id)
        expectedJson.put("userId", expectedKYCEntity?.user?.id)


        JSONAssert.assertEquals(expectedJson, JSONObject(expectedKYCJson), JSONCompareMode.STRICT)
    }

    @And("the database should have updated KYC information")
    fun kycUpdateMatchWithDb() {
        val storedKyc = kycRepository.findByIdOrNull(expectedKYCEntity?.id!!)
        assertNotNull(storedKyc)

        assertNotNull(expectedKYCJson)
        val json = JSONObject(expectedKYCJson)

        val id = json.getLong("id")
        assertNotNull(id)

        val userId = json.getLong("userId")
        assertNotNull(userId)

        val firstName = json.getString("firstName")
        assertNotNull(firstName)

        val lastName = json.getString("lastName")
        assertNotNull(lastName)

        val dateOfBirthString = json.getString("dateOfBirth")
        assertNotNull(dateOfBirthString)

        val dateOfBirth = LocalDate.parse(dateOfBirthString, kycDateFormatter)
        assertNotNull(dateOfBirth)

        val salary = BigDecimal.valueOf(json.getDouble("salary"))
        assertNotNull(salary)

        val nationality = json.getString("nationality")
        assertNotNull(nationality)

        assertEquals(id, storedKyc?.id)
        assertEquals(userId, storedKyc?.user?.id)
        assertEquals(firstName, storedKyc?.firstName)
        assertEquals(lastName, storedKyc?.lastName)
        assertEquals(dateOfBirth, storedKyc?.dateOfBirth)
        assertEquals(0, salary.compareTo(storedKyc?.salary))
        assertEquals(nationality, storedKyc?.nationality)
    }

    @When("user creates a KYC with salary {double}")
    fun userCreatesKycWithInvalidSalary(salary: Double) {
        val jsonRequest = JSONObject()
        jsonRequest.put("salary", salary)
        jsonRequest.put("firstName", "adam")
        jsonRequest.put("lastName", "smith")
        jsonRequest.put("dateOfBirth", "01-01-1990")
        jsonRequest.put("nationality", "Kuwaiti")

        response = sendRequest(
            method = "POST",
            endpoint = "",
        )

    }


    private inline fun <reified T> sendRequest(
        method: String,
        endpoint: String,
        payload: String? = null,
        jwtToken: String? = null
    ): ResponseEntity<T> {
        val requestMethod = HttpMethod.valueOf(method.uppercase())

        val headers = HttpHeaders().apply {
            if (jwtToken != null) {
                this.setBearerAuth(jwtToken)
            }
            if (payload != null) {
                this.contentType = MediaType.APPLICATION_JSON
            }
        }
        val body: Any = payload ?: ""
        val entity = HttpEntity(body, headers)

        return testRestTemplate.exchange(
            endpoint,
            requestMethod,
            entity,
            T::class.java
        )
    }

}