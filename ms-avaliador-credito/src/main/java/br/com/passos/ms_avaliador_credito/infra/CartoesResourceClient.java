package br.com.passos.ms_avaliador_credito.infra;

import br.com.passos.ms_avaliador_credito.domain.Cartao;
import br.com.passos.ms_avaliador_credito.domain.CartaoCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mscartoes", path = "/cartoes")
public interface CartoesResourceClient {

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartaoCliente>> listarCartoesByCpf(@RequestParam("cpf") String cpf);

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaMenorIgual(@RequestParam("renda") Long renda);
}
