package br.com.zup.utils.extensions

import br.com.zup.ChavePixResponse
import br.com.zup.controller.dto.response.ChavePixResponseRest

fun ChavePixResponse.toResponse() : ChavePixResponseRest {
    return ChavePixResponseRest(pixId)
}