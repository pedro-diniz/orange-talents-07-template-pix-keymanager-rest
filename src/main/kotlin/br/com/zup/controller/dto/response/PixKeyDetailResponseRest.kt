package br.com.zup.controller.dto.response

import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class PixKeyDetailResponseRest(
    val keyType: String,
    val key: String,
    val bankAccount: BankAccountResponseRest,
    val owner: OwnerResponseRest,
    val createdAt: LocalDateTime
)