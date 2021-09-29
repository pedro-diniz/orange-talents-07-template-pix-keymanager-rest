package br.com.zup.controller.dto.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class OwnerResponseRest(
    val type: String,
    val name: String,
    val taxIdNumber: String
)