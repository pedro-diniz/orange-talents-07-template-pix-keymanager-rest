package br.com.zup.controller.service

import br.com.zup.ItemChavePixResponse
import br.com.zup.ListagemChavesPixResponse
import br.com.zup.ListagemChavesPixServiceGrpc
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
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito

@MicronautTest
internal class ListagemGrpcServiceTest{

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var grpcServerMock: ListagemChavesPixServiceGrpc.ListagemChavesPixServiceBlockingStub

    @Test
    internal fun `deve retornar uma lista de registros`() {
        val clientId = "c56dfef4-7901-44fb-84e2-a2cefb157890"

        val listaGrpc = ListagemChavesPixResponse.newBuilder()
        val chave1 = ItemChavePixResponse.newBuilder()
            .setPixId(1)
            .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setKeyType("CPF")
            .setKey("07457547401")
            .setAccountType("CACC")
            .setCreatedAt(Timestamp.newBuilder().setSeconds(9999999).setNanos(999999))
            .build()
        val chave2 = ItemChavePixResponse.newBuilder()
            .setPixId(2)
            .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setKeyType("EMAIL")
            .setKey("fulano@zup.com.br")
            .setAccountType("SVGS")
            .setCreatedAt(Timestamp.newBuilder().setSeconds(8888888).setNanos(8888888))
            .build()
        listaGrpc.addListagemChavesPixResponse(chave1)
        listaGrpc.addListagemChavesPixResponse(chave2)

        Mockito.`when`(grpcServerMock.listaChaves(Mockito.any())).thenReturn(listaGrpc.build())

        val httpRequest : HttpRequest<String> = HttpRequest.GET("/api/chaves/$clientId")
        val httpResponse : HttpResponse<Any> = client.toBlocking().exchange(httpRequest)

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).listaChaves(Mockito.any())
        Mockito.reset(grpcServerMock)

        println(httpResponse.body())
        with(httpResponse) {
            assertEquals(200, code())
        }
    }

    @Test
    internal fun `deve retornar 400 se o clientId for invalido`() {
        val clientId = "70d1e3e8-2164-11ec-9621"

        Mockito.`when`(grpcServerMock.listaChaves(Mockito.any())).thenReturn(ListagemChavesPixResponse.newBuilder().build())

        val httpRequest : HttpRequest<String> = HttpRequest.GET("/api/chaves/$clientId")
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.never()).listaChaves(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.BAD_REQUEST, status)
        }
    }

    @Test
    internal fun `nao deve retornar uma lista se o clientId nao existir`() {

        val clientId = "70d1e3e8-2164-11ec-9621-0242ac130002"

        Mockito.`when`(grpcServerMock.listaChaves(Mockito.any())).thenThrow(StatusRuntimeException(Status.NOT_FOUND))

        val httpRequest : HttpRequest<String> = HttpRequest.GET("/api/chaves/$clientId")
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).listaChaves(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.NOT_FOUND, status)
        }

    }

    @Test
    internal fun `nao deve retornar uma lista se algum server estiver off`() {

        val clientId = "70d1e3e8-2164-11ec-9621-0242ac130002"

        Mockito.`when`(grpcServerMock.listaChaves(Mockito.any())).thenThrow(StatusRuntimeException(Status.UNAVAILABLE))

        val httpRequest : HttpRequest<String> = HttpRequest.GET("/api/chaves/$clientId")
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).listaChaves(Mockito.any())
        Mockito.reset(grpcServerMock)

        with(error) {
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, status)
        }
    }

    @Test
    internal fun `deve lancar excecao desconhecida`() {

        val clientId = "70d1e3e8-2164-11ec-9621-0242ac130002"

        Mockito.`when`(grpcServerMock.listaChaves(Mockito.any())).thenThrow(StatusRuntimeException(Status.INTERNAL))

        val httpRequest : HttpRequest<String> = HttpRequest.GET("/api/chaves/$clientId")
        val error = assertThrows<HttpClientResponseException> {
            val httpResponse: HttpResponse<Any> = client.toBlocking().exchange(httpRequest)
        }

        Mockito.verify(grpcServerMock, Mockito.atLeastOnce()).listaChaves(Mockito.any())
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
            Mockito.mock(ListagemChavesPixServiceGrpc.ListagemChavesPixServiceBlockingStub::class.java)
    }

}