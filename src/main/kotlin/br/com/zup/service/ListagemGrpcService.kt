package br.com.zup.service

import br.com.zup.ListagemChavesPixRequest
import br.com.zup.ListagemChavesPixServiceGrpc
import br.com.zup.utils.extensions.toResponse
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Validated @Singleton
class ListagemGrpcService(
    @Inject val grpcServer: ListagemChavesPixServiceGrpc.ListagemChavesPixServiceBlockingStub
) {

    fun listagemGrpc(request: ListagemChavesPixRequest) : HttpResponse<Any> {

        try {
            return HttpResponse.ok(grpcServer.listaChaves(request).toResponse())
        }
        catch (e: StatusRuntimeException) {
            val description = e.status.description
            val statusCode = e.status.code

            when (statusCode) {
                Status.Code.NOT_FOUND ->
                    return HttpResponse.status<Any?>(HttpStatus.NOT_FOUND).body(description)
                Status.Code.UNAVAILABLE ->
                    return HttpResponse.status<Any?>(HttpStatus.SERVICE_UNAVAILABLE).body(description)
                else ->
                    return HttpResponse.status<Any?>(HttpStatus.INTERNAL_SERVER_ERROR).body(description)
            }
        }

    }
}
