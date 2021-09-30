package br.com.zup.controller.dto.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class TitularResponseRest(
    val nome: String,
    val cpf: String
)