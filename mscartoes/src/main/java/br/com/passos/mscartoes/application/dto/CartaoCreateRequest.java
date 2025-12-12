package br.com.passos.mscartoes.application.dto;

import br.com.passos.mscartoes.domain.BandeiraCartao;
import br.com.passos.mscartoes.domain.Cartao;
import java.math.BigDecimal;

public record CartaoCreateRequest(
        String nome,
        BandeiraCartao bandeira,
        BigDecimal renda,
        BigDecimal limiteBasicao
) {
    public Cartao toModel(){
        return new Cartao(nome, bandeira, renda, limiteBasicao);
    }
}
