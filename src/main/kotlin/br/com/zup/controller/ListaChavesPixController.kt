package br.com.zup.controller

import br.com.zup.ListagemChavesPixRequest
import br.com.zup.service.ListagemGrpcService
import br.com.zup.utils.validation.UUIDValido
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import jakarta.inject.Inject

@Validated @Controller
class ListaChavesPixController(
    @Inject val listagemGrpcService: ListagemGrpcService
) {

    @Get("/api/chaves/{clientId}")
    fun listagemGrpc(@PathVariable @UUIDValido clientId: String): HttpResponse<Any> {

        val grpcRequest = ListagemChavesPixRequest.newBuilder().setClientId(clientId).build()
        return listagemGrpcService.listagemGrpc(grpcRequest)

    }
}