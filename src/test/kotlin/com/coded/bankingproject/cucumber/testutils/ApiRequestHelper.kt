package com.coded.bankingproject.cucumber.testutils

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.stereotype.Component

@Component
class ApiRequestHelper(
    private val testRestTemplate: TestRestTemplate
) {
    fun <T> sendRequest(
        method: String,
        endpoint: String,
        payload: String? = null,
        jwtToken: String? = null,
        responseType: Class<T>
    ): ResponseEntity<T> {
        val headers = HttpHeaders().apply {
            if (jwtToken != null) setBearerAuth(jwtToken)
            if (payload != null) contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(payload ?: "", headers)

        val response =  testRestTemplate.exchange(
            endpoint,
            HttpMethod.valueOf(method.uppercase()),
            entity,
            responseType
        )

        return response
    }
}
