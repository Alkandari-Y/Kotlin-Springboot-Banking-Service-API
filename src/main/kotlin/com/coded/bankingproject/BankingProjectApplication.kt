package com.coded.bankingproject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@SpringBootApplication
class BankingProjectApplication

fun main(args: Array<String>) {
    runApplication<BankingProjectApplication>(*args)
}
