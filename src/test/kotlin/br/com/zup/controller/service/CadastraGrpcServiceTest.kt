package br.com.zup.controller.service

import br.com.zup.*
import br.com.zup.controller.dto.NovaChavePixResponse
import br.com.zup.controller.dto.request.NovaChavePixRequest
import br.com.zup.utils.GrpcClientFactory
import br.com.zup.utils.extensions.toResponse
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito

@MicronautTest
internal class CadastraGrpcServiceTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var grpcServerMock: DesafioPixServiceGrpc.DesafioPixServiceBlockingStub

    @Test
    internal fun `deve retornar sucesso`() {

        val request = NovaChavePixRequest(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            TipoChave.EMAIL,
            "fulano@zup.com.br",
            TipoConta.CONTA_CORRENTE
        )

        val grpcResponse = ChavePixResponse.newBuilder().setPixId(1L).build()

        Mockito.`when`(grpcServerMock.cadastra(Mockito.any())).thenReturn(grpcResponse)

        val httpRequest = HttpRequest.POST("/api/chaves", request)
        val httpResponse : HttpResponse<Any> = client.toBlocking().exchange(httpRequest)

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).cadastra(Mockito.any())

        with(httpResponse) {
            assertEquals(201, code())
            assertTrue(httpResponse.headers.contains("Location"))
            assertTrue(httpResponse.header("Location")!!.contains(grpcResponse.pixId.toString()))
        }

    }

    @Test
    internal fun `deve retornar 400`() {

        val request = NovaChavePixRequest(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            TipoChave.EMAIL,
            "fulano@zup.com.br",
            TipoConta.CONTA_CORRENTE
        )

        Mockito.`when`(grpcServerMock.cadastra(Mockito.any())).thenThrow(StatusRuntimeException(Status.INVALID_ARGUMENT))

        val httpRequest = HttpRequest.POST("/api/chaves", request)
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse : HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).cadastra(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.BAD_REQUEST, status)
        }

    }

    @Test
    internal fun `deve retornar 422`() {

        val request = NovaChavePixRequest(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            TipoChave.CPF,
            "07457547401",
            TipoConta.CONTA_CORRENTE
        )

        Mockito.`when`(grpcServerMock.cadastra(Mockito.any())).thenThrow(StatusRuntimeException(Status.ALREADY_EXISTS))

        val httpRequest = HttpRequest.POST("/api/chaves", request)
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse : HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).cadastra(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, status)
        }

    }

    @Test
    internal fun `deve retornar 503`() {

        val request = NovaChavePixRequest(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            TipoChave.TELEFONE_CELULAR,
            "+5584996327131",
            TipoConta.CONTA_POUPANCA
        )

        Mockito.`when`(grpcServerMock.cadastra(Mockito.any())).thenThrow(StatusRuntimeException(Status.UNAVAILABLE))

        val httpRequest = HttpRequest.POST("/api/chaves", request)
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse : HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).cadastra(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, status)
        }

    }

    @Test
    internal fun `deve retornar 500`() {

        val request = NovaChavePixRequest(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            TipoChave.TELEFONE_CELULAR,
            "+5584996327131",
            TipoConta.CONTA_POUPANCA
        )

        Mockito.`when`(grpcServerMock.cadastra(Mockito.any())).thenThrow(StatusRuntimeException(Status.INTERNAL))

        val httpRequest = HttpRequest.POST("/api/chaves", request)
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse : HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).cadastra(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status)
        }

    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class Clients { // o Micronaut sempre levanta os testes em uma porta diferente. Para termos acesso a esse channel, usamos aquele enum no argumento

        @Singleton
        fun blockingStubMock() = Mockito.mock(DesafioPixServiceGrpc.DesafioPixServiceBlockingStub::class.java)

    }

}