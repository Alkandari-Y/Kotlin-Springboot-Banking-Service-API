package com.coded.bankingproject.cucumber.testcontext

import org.springframework.stereotype.Component
import org.springframework.http.ResponseEntity

@Component
class TestContext {
    var token: String = ""
    var response: ResponseEntity<String>? = null
    var requestBody: String = ""
    var expectedUserName: String = ""
    var accountNumber: String = ""
}
