package br.com.markus.exception;

/**
 * Classe de Exceção da Transacao
 */
public class TransacaoInvalidaException extends Exception {
    public TransacaoInvalidaException() {
    }

    public TransacaoInvalidaException(String message) {
        super(message);
    }

    public TransacaoInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransacaoInvalidaException(Throwable cause) {
        super(cause);
    }
}