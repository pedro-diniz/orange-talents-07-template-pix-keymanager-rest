package br.com.zup.controller.service

import br.com.zup.BankAccount
import br.com.zup.ConsultaChavePixResponse
import br.com.zup.ConsultaChavePixServiceGrpc
import br.com.zup.Owner
import br.com.zup.controller.dto.request.ConsultaChavePixRequestRest
import br.com.zup.controller.dto.request.ExclusaoChavePixRequestRest
import br.com.zup.utils.GrpcClientFactory
import com.google.protobuf.Timestamp
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
import org.mockito.Mockito

@MicronautTest
internal class ConsultaGrpcServiceTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var grpcServerMock: ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub

    @Test
    internal fun `deve retornar um registro`() {
        val request = ConsultaChavePixRequestRest(
            pixId = 1,
            clientId = "c56dfef4-7901-44fb-84e2-a2cefb157890",
            chavePix = "07457547401"
        )

        val grpcResponse = ConsultaChavePixResponse.newBuilder()
            .setKeyType("CPF")
            .setKey("07457547401")
            .setBankAccount(BankAccount.newBuilder()
                .setParticipant("60701190")
                .setBranch("0001")
                .setAccountNumber("123456")
                .setAccountType("SVGS"))
            .setOwner(Owner.newBuilder()
                .setType("NATURAL_PERSON")
                .setName("Pedro Diniz")
                .setTaxIdNumber("07457547401"))
            .setCreatedAt(Timestamp.newBuilder().setSeconds(9999999).setNanos(999999))
            .build()

        Mockito.`when`(grpcServerMock.consultaChave(Mockito.any())).thenReturn(grpcResponse)

        val httpRequest = HttpRequest.POST("/api/chaves/consulta", request)
        val httpResponse : HttpResponse<Any> = client.toBlocking().exchange(httpRequest)

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).consultaChave(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(httpResponse) {
            assertEquals(200, code())
        }

    }

    @Test
    internal fun `nao deve retornar um registro se chave nao pertence a cliente`() {
        val request = ConsultaChavePixRequestRest(
            pixId = 1,
            clientId = "c56dfef4-7901-44fb-84e2-a2cefb157890",
            chavePix = null
        )

        Mockito.`when`(grpcServerMock.consultaChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.PERMISSION_DENIED))

        val httpRequest = HttpRequest.POST("/api/chaves/consulta", request)
        val error = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).consultaChave(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.FORBIDDEN, status)
        }

    }

    @Test
    internal fun `nao deve retornar um registro se a chave nao existir`() {
        val request = ConsultaChavePixRequestRest(
            pixId = 1,
            clientId = "c56dfef4-7901-44fb-84e2-a2cefb157890",
            chavePix = null
        )

        Mockito.`when`(grpcServerMock.consultaChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.NOT_FOUND))

        val httpRequest = HttpRequest.POST("/api/chaves/consulta", request)
        val error = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).consultaChave(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.NOT_FOUND, status)
        }

    }

    @Test
    internal fun `nao deve retornar um registro se algum servico estiver fora do ar`() {
        val request = ConsultaChavePixRequestRest(
            pixId = null,
            clientId = null,
            chavePix = "07457547401"
        )

        Mockito.`when`(grpcServerMock.consultaChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.UNAVAILABLE))

        val httpRequest = HttpRequest.POST("/api/chaves/consulta", request)
        val error = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).consultaChave(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, status)
        }
    }

    @Test
    internal fun `deve lancar excecao de modalidade de busca invalida`() {
        val request = ConsultaChavePixRequestRest(
            pixId = 1,
            clientId = null,
            chavePix = null
        )

        Mockito.`when`(grpcServerMock.consultaChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.INVALID_ARGUMENT))

        val httpRequest = HttpRequest.POST("/api/chaves/consulta", request)
        val error = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).consultaChave(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.BAD_REQUEST, status)
        }

    }

    @Test
    internal fun `deve lancar erro interno`() {
        val request = ConsultaChavePixRequestRest(
            pixId = null,
            clientId = null,
            chavePix = "+5584996327131"
        )

        Mockito.`when`(grpcServerMock.consultaChave(Mockito.any())).thenThrow(StatusRuntimeException(Status.INTERNAL))

        val httpRequest = HttpRequest.POST("/api/chaves/consulta", request)
        val error = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).consultaChave(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class Clients { // o Micronaut sempre levanta os testes em uma porta diferente. Para termos acesso a esse channel, usamos aquele enum no argumento

        @Singleton
        fun blockingStubMock() =
            Mockito.mock(ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub::class.java)
    }
}