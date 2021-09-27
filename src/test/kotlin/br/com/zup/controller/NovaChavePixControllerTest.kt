package br.com.zup.controller

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.controller.dto.request.NovaChavePixRequest
import br.com.zup.controller.service.CadastraGrpcService
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito

@MicronautTest
internal class NovaChavePixControllerTest(
    val novaChavePixController: NovaChavePixController,
    val cadastraGrpcService: CadastraGrpcService
) {

    @Test
    internal fun `deve cadastrar uma chave pix`() {

        val request = NovaChavePixRequest("c56dfef4-7901-44fb-84e2-a2cefb157890",
            TipoChave.TELEFONE_CELULAR,
            "+5584999998887",
            TipoConta.CONTA_POUPANCA)

        Mockito.`when`(cadastraGrpcService.cadastraGrpc(request.toGrpcRequest()))
            .thenReturn(HttpResponse.created("/api/chaves/1"))

        val response = novaChavePixController.cadastrar(request)

        with(response) {
            assertEquals(201, code())
        }
    }

    @Test
    internal fun `nao deve cadastrar chave pix duplicada`() {
        TODO("Not yet implemented")
    }

    @Test
    internal fun `nao deve cadastrar chave pix com input invalido`() {
        TODO("Not yet implemented")
    }

    @Test
    internal fun `nao deve cadastrar chave pix se server estiver fora do ar`() {
        TODO("Not yet implemented")
    }

    @Test
    internal fun `deve lancar erro 500`() {
        TODO("Not yet implemented")
    }

    @MockBean(CadastraGrpcService::class) // bean a ser mockado
    fun cadastraGrpcService() : CadastraGrpcService {
        return Mockito.mock(CadastraGrpcService::class.java)
    }
}