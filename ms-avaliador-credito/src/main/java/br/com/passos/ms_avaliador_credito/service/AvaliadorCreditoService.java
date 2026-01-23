package br.com.passos.ms_avaliador_credito.service;

import br.com.passos.ms_avaliador_credito.application.exception.DadosClienteNotFoundException;
import br.com.passos.ms_avaliador_credito.application.exception.ErroComunicacaoMicroservicesException;
import br.com.passos.ms_avaliador_credito.domain.CartaoCliente;
import br.com.passos.ms_avaliador_credito.domain.DadosCliente;
import br.com.passos.ms_avaliador_credito.domain.SituacaoCliente;
import br.com.passos.ms_avaliador_credito.infra.CartoesResourceClient;
import br.com.passos.ms_avaliador_credito.infra.ClienteResourceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.getByCpf(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesResourceClient.listarCartoesByCpf(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
        } catch (FeignException.FeignClientException feignClientException){
            int status = feignClientException.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(feignClientException.getMessage(), status);
        }

    }
}
