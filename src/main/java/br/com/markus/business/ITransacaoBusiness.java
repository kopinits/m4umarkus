package br.com.markus.business;

import br.com.markus.exception.TransacaoInvalidaException;
import br.com.markus.model.Transacao;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface para o TransacaoBusiness
 */
public interface ITransacaoBusiness {
    Transacao solicitarPagamento(Transacao transacao) throws TransacaoInvalidaException;

    void solicitarPagamentoAssincrono(Transacao transacao) throws TransacaoInvalidaException;

    Transacao cancelarPagamento(Transacao transacao) throws TransacaoInvalidaException;

    ArrayList<Transacao> consultarPagamentos(List<Transacao> transacaoList);

    ArrayList<TransacaoInvalidaException> validarTransacao(Transacao transacao);

    void processarTransacoesAssincronas();

}
