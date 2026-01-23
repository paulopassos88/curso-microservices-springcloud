package br.com.passos.ms_avaliador_credito.application;

import br.com.passos.ms_avaliador_credito.application.exception.DadosClienteNotFoundException;
import br.com.passos.ms_avaliador_credito.application.exception.ErroComunicacaoMicroservicesException;
import br.com.passos.ms_avaliador_credito.domain.DadosAvalicao;
import br.com.passos.ms_avaliador_credito.domain.RetornoAvaliacaoCliente;
import br.com.passos.ms_avaliador_credito.domain.SituacaoCliente;
import br.com.passos.ms_avaliador_credito.service.AvaliadorCreditoService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status(){
        return "OK";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf){
        try {
            SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClienteNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e){
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvalicao dados){
        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorCreditoService.realizarAvaliacao(dados.getCpf(), dados.getRenda());
            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (DadosClienteNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e){
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }
}
