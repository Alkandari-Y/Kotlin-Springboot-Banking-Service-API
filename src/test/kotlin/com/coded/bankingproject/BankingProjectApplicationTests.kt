package com.coded.bankingproject

import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest

@CucumberContextConfiguration
@SpringBootTest(
    classes = [BankingProjectApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class BankingProjectApplicationTests {}
