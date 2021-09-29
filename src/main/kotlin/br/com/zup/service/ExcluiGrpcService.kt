package br.com.zup.service

import br.com.zup.ExclusaoChavePixRequest
import br.com.zup.ExclusaoChavePixServiceGrpc
import br.com.zup.utils.extensions.toResponse
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Validated @Singleton
class ExcluiGrpcService(@Inject val grpcServer: ExclusaoChavePixServiceGrpc.ExclusaoChavePixServiceBlockingStub
) {

    fun excluiGrpc(request: ExclusaoChavePixRequest) : HttpResponse<Any> {

        try {
            return HttpResponse.ok(grpcServer.excluiChave(request).toResponse())
        }
        catch (e: StatusRuntimeException) {
            val description = e.status.description
            val statusCode = e.status.code

            when(statusCode) {
                Status.Code.PERMISSION_DENIED ->
                    return HttpResponse.status<Any?>(HttpStatus.FORBIDDEN).body(description)
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