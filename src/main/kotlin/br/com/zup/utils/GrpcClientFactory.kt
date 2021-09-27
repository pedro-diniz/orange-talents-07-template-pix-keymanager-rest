package br.com.zup.utils

import br.com.zup.ConsultaChavePixServiceGrpc
import br.com.zup.DesafioPixServiceGrpc
import br.com.zup.ExclusaoChavePixServiceGrpc
import br.com.zup.ListagemChavesPixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory // fabrica o bean do nosso client
class GrpcClientFactory {

    @Singleton // passo o channel no arg. do método, p/ o Micronaut tomar conta dele ao invés de instanciar manualmente
    fun novaChaveClientStub(
        @GrpcChannel(value = "chavepix") channel: ManagedChannel // channel configurado no application.yml
    ) : DesafioPixServiceGrpc.DesafioPixServiceBlockingStub? {

        return DesafioPixServiceGrpc.newBlockingStub(channel)

    }

    @Singleton // passo o channel no arg. do método, p/ o Micronaut tomar conta dele ao invés de instanciar manualmente
    fun exclusaoChaveClientStub(
        @GrpcChannel(value = "chavepix") channel: ManagedChannel // channel configurado no application.yml
    ) : ExclusaoChavePixServiceGrpc.ExclusaoChavePixServiceBlockingStub? {

        return ExclusaoChavePixServiceGrpc.newBlockingStub(channel)

    }

    @Singleton // passo o channel no arg. do método, p/ o Micronaut tomar conta dele ao invés de instanciar manualmente
    fun consultaChaveClientStub(
        @GrpcChannel(value = "chavepix") channel: ManagedChannel // channel configurado no application.yml
    ) : ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub? {

        return ConsultaChavePixServiceGrpc.newBlockingStub(channel)

    }

    @Singleton // passo o channel no arg. do método, p/ o Micronaut tomar conta dele ao invés de instanciar manualmente
    fun listaChavesClientStub(
        @GrpcChannel(value = "chavepix") channel: ManagedChannel // channel configurado no application.yml
    ) : ListagemChavesPixServiceGrpc.ListagemChavesPixServiceBlockingStub? {

        return ListagemChavesPixServiceGrpc.newBlockingStub(channel)

    }

}