package br.com.passos.msclientes.application.dto;

import br.com.passos.msclientes.domain.Cliente;

public record ClienteCreateRequest(
        String nome, String cpf, Integer idade
) {
    public Cliente toModel() {
        return new Cliente(nome, cpf, idade);
    }
}
