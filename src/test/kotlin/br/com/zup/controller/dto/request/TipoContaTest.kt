package br.com.zup.controller.dto.request

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TipoContaTest {

    @Test
    internal fun `deve ser uma conta corrente`() {
        assertEquals(TipoConta.CONTA_CORRENTE.toString(), "CONTA_CORRENTE")
    }

    @Test
    internal fun `deve ser uma conta poupanca`() {
        assertEquals(TipoConta.CONTA_POUPANCA.toString(), "CONTA_POUPANCA")
    }

}