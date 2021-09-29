package br.com.zup.controller

import br.com.zup.controller.dto.request.ExclusaoChavePixRequestRest
import br.com.zup.service.ExcluiGrpcService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated @Controller
class ExclusaoChavePixController(
    @Inject val excluiGrpcService: ExcluiGrpcService
) {

    private val logger = LoggerFactory.getLogger(ExclusaoChavePixController::class.java)

    @Delete("/api/chaves")
    fun excluir(@Valid request: ExclusaoChavePixRequestRest) : HttpResponse<Any> {

        val grpcRequest = request.toGrpcRequest()
        return excluiGrpcService.excluiGrpc(grpcRequest)

    }
}