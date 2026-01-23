package br.com.passos.ms_avaliador_credito.service;

import br.com.passos.ms_avaliador_credito.application.exception.DadosClienteNotFoundException;
import br.com.passos.ms_avaliador_credito.application.exception.ErroComunicacaoMicroservicesException;
import br.com.passos.ms_avaliador_credito.domain.*;
import br.com.passos.ms_avaliador_credito.infra.CartoesResourceClient;
import br.com.passos.ms_avaliador_credito.infra.ClienteResourceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.getByCpf(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesResourceClient.getCartoesRendaMenorIgual(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();

            var listaCartoesAprovados = cartoes.stream().map(cartao -> {

                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal rendaBD = BigDecimal.valueOf(renda);
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.idade());
                var fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        } catch (FeignException.FeignClientException feignClientException){
            int status = feignClientException.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(feignClientException.getMessage(), status);
        }
    }
}
