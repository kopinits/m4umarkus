package br.com.markus.dao;

import br.com.markus.exception.TransacaoInvalidaException;
import br.com.markus.model.Transacao;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface para o TransacaoDAO
 */
public interface ITransacaoDAO {
    Transacao solicitarAutorizacao(Transacao transacao, boolean sincrona) throws TransacaoInvalidaException;

    Transacao cancelarTransacao(Transacao transacao) throws TransacaoInvalidaException;

    ArrayList<Transacao> consultarTransacao(List<Transacao> transacaoList);

    void processarTransacoesAssincronas();
}
