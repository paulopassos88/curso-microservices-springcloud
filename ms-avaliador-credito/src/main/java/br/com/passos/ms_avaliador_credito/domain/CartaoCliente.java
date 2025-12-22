package br.com.passos.ms_avaliador_credito.domain;

import java.math.BigDecimal;

public record CartaoCliente(
        String nome,
        String bandeira,
        BigDecimal limiteLiberado
) {
}
