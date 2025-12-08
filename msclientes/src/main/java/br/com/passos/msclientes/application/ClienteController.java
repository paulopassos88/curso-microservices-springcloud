package br.com.passos.msclientes.application;

import br.com.passos.msclientes.application.dto.ClienteCreateRequest;
import br.com.passos.msclientes.domain.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ClienteCreateRequest request){
        var cliente = request.toModel();
        service.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED).location(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<Cliente> getByCpf(@RequestParam("cpf") String cpf){
        return service.getByCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
