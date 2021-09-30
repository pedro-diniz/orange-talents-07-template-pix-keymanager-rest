package br.com.zup.controller.dto.request

import br.com.zup.ConsultaChavePixRequest
import br.com.zup.utils.validation.UUIDValido
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Positive

@Introspected
data class ConsultaChavePixRequestRest(
    @field:Positive val pixId: Long?,
    @field:UUIDValido val clientId: String?,
    val chavePix: String?
) {

    fun toGrpcRequest() : ConsultaChavePixRequest {

        val grpcRequest = ConsultaChavePixRequest.newBuilder()
            .setPixId(pixId ?: 0)
            .setChavePix(chavePix ?: "")

        if (!clientId.isNullOrBlank()) {
            grpcRequest.setClientId(clientId)
        }

        return grpcRequest.build()
    }

}