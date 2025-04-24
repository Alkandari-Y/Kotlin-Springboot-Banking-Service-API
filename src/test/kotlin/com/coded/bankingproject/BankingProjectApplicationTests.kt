package com.coded.bankingproject

import io.cucumber.spring.CucumberContextConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@CucumberContextConfiguration
@SpringBootTest(
    classes = [BankingProjectApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class BankingProjectApplicationTests {

}
