package br.com.zup.utils.extensions

import br.com.zup.ChavePixResponse
import br.com.zup.controller.dto.NovaChavePixResponse

fun ChavePixResponse.toResponse() : NovaChavePixResponse {
    return NovaChavePixResponse(pixId)
}