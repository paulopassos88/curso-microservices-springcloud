package br.com.passos.mscartoes.application.representation;

import br.com.passos.mscartoes.domain.ClienteCartao;
import java.math.BigDecimal;

public record CartoesPorClienteResponse(
        String nome,
        String bandeira,
        BigDecimal limiteLiberado
) {
    public static CartoesPorClienteResponse fromModel(ClienteCartao model) {
        return new CartoesPorClienteResponse(
                model.getCartao().getNome(),
                model.getCartao().getBandeira().toString(),
                model.getLimite()
        );
    }
}
