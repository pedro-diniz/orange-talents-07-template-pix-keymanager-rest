package br.com.zup.controller

import br.com.zup.controller.dto.response.ChavePixResponseRest
import br.com.zup.controller.dto.request.ChavePixRequestRest
import br.com.zup.controller.service.CadastraGrpcService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated @Controller
class NovaChavePixController(
    @Inject val service: CadastraGrpcService
) {

    private val logger = LoggerFactory.getLogger(NovaChavePixController::class.java)

    @Post("/api/chaves")
    fun cadastrar(@Valid request: ChavePixRequestRest): HttpResponse<Any> {

        val grpcRequest = request.toGrpcRequest()
        println(grpcRequest.toString())

        val response = service.cadastraGrpc(grpcRequest)

        if (response.code() != 201) {
            return response
        }
        else {
            val pixId = (response.body() as ChavePixResponseRest).pixId
            println("pixId: $pixId")
            val uri = UriBuilder.of("/api/chaves/{pixId}")
                .expand(mutableMapOf(Pair("pixId", pixId)))
            println("Uri: $uri")

            return HttpResponse.created(uri)
        }

    }

}