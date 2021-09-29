package br.com.zup.controller.service

import br.com.zup.ConfirmaExclusaoResponse
import br.com.zup.ExclusaoChavePixServiceGrpc
import br.com.zup.controller.dto.request.ExclusaoChavePixRequestRest
import br.com.zup.utils.GrpcClientFactory
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito

@MicronautTest
internal class ExcluiGrpcServiceTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var grpcServerMock: ExclusaoChavePixServiceGrpc.ExclusaoChavePixServiceBlockingStub

    @Test
    internal fun `deve excluir um registro`() {
        val request = ExclusaoChavePixRequestRest(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "+5584996327131"
        )

        val grpcResponse = ConfirmaExclusaoResponse.newBuilder().setMensagem("Chave pix excluída com sucesso!").build()

        Mockito.`when`(grpcServerMock.excluiChave(Mockito.any())).thenReturn(grpcResponse)

        val httpRequest = HttpRequest.DELETE("/api/chaves", request)
        val httpResponse : HttpResponse<Any> = client.toBlocking().exchange(httpRequest)

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).excluiChave(Mockito.any())

        with(httpResponse) {
            assertEquals(200, code())
        }
    }

    @Test
    internal fun `nao deve excluir um registro se chave nao pertence a cliente`() {
        val request = ExclusaoChavePixRequestRest(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "+5584996327131"
        )

        Mockito.`when`(grpcServerMock.excluiChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.PERMISSION_DENIED))

        val httpRequest = HttpRequest.DELETE("/api/chaves", request)
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).excluiChave(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.FORBIDDEN, status)
        }
    }

    @Test
    internal fun `nao deve excluir um registro se a chave nao existir`() {
        val request = ExclusaoChavePixRequestRest(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "+5584996327131"
        )

        Mockito.`when`(grpcServerMock.excluiChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.NOT_FOUND))

        val httpRequest = HttpRequest.DELETE("/api/chaves", request)
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).excluiChave(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.NOT_FOUND, status)
        }
    }

    @Test
    internal fun `nao deve excluir um registro se o server grpc está off`() {
        val request = ExclusaoChavePixRequestRest(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "+5584996327131"
        )

        Mockito.`when`(grpcServerMock.excluiChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.UNAVAILABLE))

        val httpRequest = HttpRequest.DELETE("/api/chaves", request)
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).excluiChave(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, status)
        }
    }

    @Test
    internal fun `deve lancar erro interno`() {
        val request = ExclusaoChavePixRequestRest(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "+5584996327131"
        )

        Mockito.`when`(grpcServerMock.excluiChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.INTERNAL))

        val httpRequest = HttpRequest.DELETE("/api/chaves", request)
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).excluiChave(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class Clients { // o Micronaut sempre levanta os testes em uma porta diferente. Para termos acesso a esse channel, usamos aquele enum no argumento

        @Singleton
        fun blockingStubMock() = Mockito.mock(ExclusaoChavePixServiceGrpc.ExclusaoChavePixServiceBlockingStub::class.java)

    }

}