package br.com.zup.controller.dto.request

import br.com.zup.ChavePixRequest
import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.pix.ChavePixValida
import br.com.zup.utils.validation.UUIDValido
import io.micronaut.core.annotation.Introspected
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected @ChavePixValida
data class ChavePixRequestRest(
    @field:UUIDValido val clientId: String,
    @field:NotNull @Enumerated(EnumType.STRING) val tipoChave: TipoChave,
    @field:Size(max=77) var chavePix: String? = null,
    @field:NotNull @Enumerated(EnumType.STRING) val tipoConta: TipoConta
) {

    fun toGrpcRequest(): ChavePixRequest {
        return ChavePixRequest.newBuilder()
            .setClientId(clientId)
            .setTipoChave(tipoChave)
            .setChavePix(chavePix)
            .setTipoConta(tipoConta)
            .build()
    }

}