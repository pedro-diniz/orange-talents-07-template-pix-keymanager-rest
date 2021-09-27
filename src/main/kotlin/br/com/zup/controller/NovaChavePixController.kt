package br.com.zup.controller

import br.com.zup.ChavePixResponse
import br.com.zup.controller.dto.request.NovaChavePixRequest
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
    fun cadastrar(@Valid request: NovaChavePixRequest): HttpResponse<Any> {

        val grpcRequest = request.toGrpcRequest()
        println(grpcRequest.toString())

        val response = service.cadastraGrpc(grpcRequest)

        if (response.code() != 200) {
            return response
        }
        else {
            val uri = UriBuilder.of("/api/chaves/{pixId}")
                .expand(mutableMapOf(Pair("pixId", (response.body() as ChavePixResponse).pixId)))

            return HttpResponse.created(uri)
        }

    }

}