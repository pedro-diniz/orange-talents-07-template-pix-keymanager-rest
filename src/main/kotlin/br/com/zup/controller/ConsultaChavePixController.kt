package br.com.zup.controller

import br.com.zup.controller.dto.request.ConsultaChavePixRequestRest
import br.com.zup.controller.service.ConsultaGrpcService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated @Controller
class ConsultaChavePixController(
    @Inject val consultaGrpcService: ConsultaGrpcService
) {

    private val logger = LoggerFactory.getLogger(ConsultaChavePixController::class.java)

    @Post("/api/chaves/consulta")
    fun consultar(@Valid request: ConsultaChavePixRequestRest) : HttpResponse<Any> {

        val grpcRequest = request.toGrpcRequest()
        return consultaGrpcService.consultaGrpc(grpcRequest)

    }

}