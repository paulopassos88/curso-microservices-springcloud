package br.com.passos.ms_avaliador_credito.service;

import br.com.passos.ms_avaliador_credito.domain.DadosCliente;
import br.com.passos.ms_avaliador_credito.domain.SituacaoCliente;
import br.com.passos.ms_avaliador_credito.infra.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;

    public SituacaoCliente obterSituacaoCliente(String cpf){
        ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.getByCpf(cpf);

        return SituacaoCliente
                .builder()
                .cliente(dadosClienteResponse.getBody())
                .build();

    }
}
