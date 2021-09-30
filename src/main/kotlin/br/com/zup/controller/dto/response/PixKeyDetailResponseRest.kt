package br.com.zup.controller.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected @JsonInclude(JsonInclude.Include.NON_NULL)
data class PixKeyDetailResponseRest(
    val pixId: Long?,
    val clientId: String?,
    val keyType: String,
    val key: String,
    val conta: ContaResponseRest,
    val createdAt: LocalDateTime
)