package br.com.zup.controller.service

import br.com.zup.ChavePixRequest
import br.com.zup.DesafioPixServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Validated @Singleton
class CadastraGrpcService(
    @Inject val grpcClient: DesafioPixServiceGrpc.DesafioPixServiceBlockingStub
) {

    fun cadastraGrpc(request: ChavePixRequest) : HttpResponse<Any> {

        try {
            return HttpResponse.ok(grpcClient.cadastra(request))
        }
        catch (e: StatusRuntimeException) {
            val description = e.status.description
            val statusCode = e.status.code

            when(statusCode) {
                Status.Code.ALREADY_EXISTS ->
                    return HttpResponse.status<Any?>(HttpStatus.UNPROCESSABLE_ENTITY).body(description)
                Status.Code.NOT_FOUND ->
                    return HttpResponse.status<Any?>(HttpStatus.NOT_FOUND).body(description)
                Status.Code.INVALID_ARGUMENT ->
                    return HttpResponse.status<Any?>(HttpStatus.BAD_REQUEST).body(description)
                Status.Code.UNAVAILABLE ->
                    return HttpResponse.status<Any?>(HttpStatus.SERVICE_UNAVAILABLE).body(description)
                else ->
                    return HttpResponse.status<Any?>(HttpStatus.INTERNAL_SERVER_ERROR).body(description)
            }
        }
    }

}