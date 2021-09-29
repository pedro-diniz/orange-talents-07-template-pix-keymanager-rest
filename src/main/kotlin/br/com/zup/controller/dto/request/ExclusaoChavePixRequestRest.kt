package br.com.zup.controller.dto.request

import br.com.zup.ExclusaoChavePixRequest
import br.com.zup.utils.validation.UUIDValido
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class ExclusaoChavePixRequestRest(
    @field:NotBlank @UUIDValido val clientId: String,
    @field:NotBlank @field:Size(max=77) val chavePix: String
) {

    fun toGrpcRequest() : ExclusaoChavePixRequest {
        return ExclusaoChavePixRequest.newBuilder()
            .setClientId(clientId)
            .setChavePix(chavePix)
            .build()
    }

}