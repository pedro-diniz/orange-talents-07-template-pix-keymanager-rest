package br.com.zup.utils

import br.com.zup.DesafioPixServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

@MicronautTest
internal class GrpcClientFactoryTest(
    @GrpcChannel(value = "chavepix") val channel: ManagedChannel // channel configurado no application.yml
) {

    @Test
    fun novaChaveClientStub() {
        val stub = GrpcClientFactory().novaChaveClientStub(channel)

        assertEquals("localhost:50051", stub!!.channel.authority())
        assertFalse(channel.isShutdown)
    }

    @Test
    fun exclusaoChaveClientStub() {
        val stub = GrpcClientFactory().exclusaoChaveClientStub(channel)

        assertEquals("localhost:50051", stub!!.channel.authority())
        assertFalse(channel.isShutdown)
    }

    @Test
    fun consultaChaveClientStub() {
        val stub = GrpcClientFactory().consultaChaveClientStub(channel)

        assertEquals("localhost:50051", stub!!.channel.authority())
        assertFalse(channel.isShutdown)
    }

    @Test
    fun listaChavesClientStub() {
        val stub = GrpcClientFactory().listaChavesClientStub(channel)

        assertEquals("localhost:50051", stub!!.channel.authority())
        assertFalse(channel.isShutdown)
    }
}