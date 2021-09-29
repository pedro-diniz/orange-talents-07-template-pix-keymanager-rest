package br.com.zup.utils.extensions

import br.com.zup.ConfirmaExclusaoResponse
import br.com.zup.controller.dto.response.ConfirmaExclusaoResponseRest

fun ConfirmaExclusaoResponse.toResponse() : ConfirmaExclusaoResponseRest {
    return ConfirmaExclusaoResponseRest(mensagem)
}