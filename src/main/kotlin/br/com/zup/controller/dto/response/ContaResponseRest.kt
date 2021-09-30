package br.com.zup.controller.dto.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class ContaResponseRest(
    val titular: TitularResponseRest,
    val nomeInstituicao: String,
    val agencia: String,
    val numeroConta: String,
    val tipoConta: String
)