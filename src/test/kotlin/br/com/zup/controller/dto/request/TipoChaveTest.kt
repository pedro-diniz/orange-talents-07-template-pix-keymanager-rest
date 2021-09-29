package br.com.zup.controller.dto.request

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TipoChaveTest {

    @Test
    internal fun `deve ser um CPF`() {
        assertEquals(TipoChave.CPF.toString(), "CPF")
    }

    @Test
    internal fun `deve ser um telefone`() {
        assertEquals(TipoChave.TELEFONE_CELULAR.toString(), "TELEFONE_CELULAR")
    }

    @Test
    internal fun `deve ser um email`() {
        assertEquals(TipoChave.EMAIL.toString(), "EMAIL")
    }

    @Test
    internal fun `deve ser uma chave aleatoria`() {
        assertEquals(TipoChave.CHAVE_ALEATORIA.toString(), "CHAVE_ALEATORIA")
    }


}