package br.com.passos.ms_avaliador_credito.application.exception;

import lombok.Getter;

public class ErroComunicacaoMicroservicesException extends RuntimeException {

    @Getter
    private Integer status;

    public ErroComunicacaoMicroservicesException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
