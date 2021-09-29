package br.com.zup.utils.extensions

import br.com.zup.ListagemChavesPixResponse
import br.com.zup.controller.dto.response.ItemChavePixResponseRest
import java.time.Instant
import java.time.ZoneId

fun ListagemChavesPixResponse.toResponse() : List<ItemChavePixResponseRest> {

    val listaChavesRest = mutableListOf<ItemChavePixResponseRest>()

    for (item in listagemChavesPixResponseList) {
        val chavePixRest = ItemChavePixResponseRest(
            pixId = item.pixId,
            clientId = item.clientId,
            keyType = item.keyType,
            key = item.key,
            accountType = item.accountType,
            createdAt = Instant.ofEpochSecond(item.createdAt.seconds, item.createdAt.nanos.toLong()).atZone(ZoneId.of("UTC")).toLocalDateTime()
        )

        listaChavesRest.add(chavePixRest)
    }

    return listaChavesRest

}