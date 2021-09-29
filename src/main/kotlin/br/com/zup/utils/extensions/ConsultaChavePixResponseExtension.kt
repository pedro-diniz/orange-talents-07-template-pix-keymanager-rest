package br.com.zup.utils.extensions

import br.com.zup.ConsultaChavePixResponse
import br.com.zup.controller.dto.response.BankAccountResponseRest
import br.com.zup.controller.dto.response.OwnerResponseRest
import br.com.zup.controller.dto.response.PixKeyDetailResponseRest
import java.time.Instant
import java.time.ZoneId

fun ConsultaChavePixResponse.toResponse() : PixKeyDetailResponseRest {
    return PixKeyDetailResponseRest(
        keyType = keyType,
        key = key,
        bankAccount = BankAccountResponseRest(
            participant = bankAccount.participant,
            branch = bankAccount.branch,
            accountNumber = bankAccount.accountNumber,
            accountType = bankAccount.accountType
        ),
        owner = OwnerResponseRest(
            type = owner.type,
            name = owner.name,
            taxIdNumber = owner.taxIdNumber
        ),
        createdAt = Instant.ofEpochSecond(createdAt.seconds, createdAt.nanos.toLong()).atZone(ZoneId.of("UTC")).toLocalDateTime()
    )
}