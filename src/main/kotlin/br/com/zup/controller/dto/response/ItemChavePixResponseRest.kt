package br.com.zup.controller.dto.response

import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class ItemChavePixResponseRest(
    val pixId: Long,
    val clientId: String,
    val keyType: String,
    val key: String,
    val accountType: String,
    val createdAt: LocalDateTime
)