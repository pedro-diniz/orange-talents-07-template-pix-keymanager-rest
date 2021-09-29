package br.com.zup.controller.dto.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class BankAccountResponseRest(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: String
)