package br.com.passos.mscartoes.application;

import br.com.passos.mscartoes.application.representation.CartaoCreateRequest;
import br.com.passos.mscartoes.application.representation.CartoesPorClienteResponse;
import br.com.passos.mscartoes.domain.Cartao;
import br.com.passos.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
public class CartaoController {

    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status(){
        return "ok";
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody CartaoCreateRequest request){
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaMenorIgual(@RequestParam("renda") Long renda){
        List<Cartao> cartoes = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.status(HttpStatus.OK).body(cartoes);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> listarCartoesByCpf(@RequestParam("cpf") String cpf){
        List<ClienteCartao> list = clienteCartaoService.listCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> responseList = list.stream()
                .map(CartoesPorClienteResponse::fromModel)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
