package br.com.markus.exception;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Excecao para o envio de múltiplas mensagens de erro.
 */
public class MultipleTransacaoInvalidaException extends RuntimeException {

    /**
     * Serial
     */
    private static final long serialVersionUID = 7139972070262446093L;

    /**
     * Lista de excecoes
     */
    private Set<TransacaoInvalidaException> exceptions = new HashSet<TransacaoInvalidaException>();

    /**
     * Constroi uma instancia desta classe.
     *
     * @param message mensagem de erro
     */
    public MultipleTransacaoInvalidaException(TransacaoInvalidaException message) {
        exceptions.add(message);
    }

    /**
     * Constroi uma instancia desta classe.
     *
     * @param messageList mensagem de erro
     */
    public MultipleTransacaoInvalidaException(Collection<? extends TransacaoInvalidaException> messageList) {
        exceptions.addAll(messageList);
    }

    /**
     * Construtor padrao
     */
    public MultipleTransacaoInvalidaException() {
    }

    /**
     * Setter dos parametros para formatacao da mensagem.
     *
     * @param message a mensagem de detalhe
     */
    public void addException(TransacaoInvalidaException message) {
        exceptions.add(message);
    }

    /**
     * Setter dos parametros para formatacao da mensagem.
     *
     * @param messageList a mensagem de detalhe
     */
    public void addExceptionList(Collection<? extends TransacaoInvalidaException> messageList) {
        exceptions.addAll(messageList);
    }

    public Set<TransacaoInvalidaException> getExceptions() {
        return exceptions;
    }

    public boolean contains(TransacaoInvalidaException ex) {
        return (null != exceptions && !exceptions.isEmpty()) && exceptions.contains(ex);
    }
}
