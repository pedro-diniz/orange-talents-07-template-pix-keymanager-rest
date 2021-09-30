package br.com.zup.utils.extensions

import br.com.zup.ConsultaChavePixResponse
import br.com.zup.controller.dto.response.*
import java.time.Instant
import java.time.ZoneId

fun ConsultaChavePixResponse.toResponse() : PixKeyDetailResponseRest {

    var pixIdResponse : Long? = null
    var clientIdResponse : String? = null

    if (pixId != 0L) {
        pixIdResponse = pixId
    }
    if (clientId.isNotBlank()) {
        clientIdResponse = clientId
    }

    return PixKeyDetailResponseRest(
        pixId = pixIdResponse,
        clientId = clientIdResponse,
        keyType = keyType,
        key = key,
        ContaResponseRest(
            TitularResponseRest(
                nome = conta.titular.nome,
                cpf = conta.titular.cpf
            ),
            agencia = conta.agencia,
            nomeInstituicao = conta.nomeInstituicao,
            numeroConta = conta.numeroConta,
            tipoConta = conta.tipoConta
        ),
        createdAt = Instant.ofEpochSecond(createdAt.seconds, createdAt.nanos.toLong()).atZone(ZoneId.of("UTC")).toLocalDateTime()
    )
}