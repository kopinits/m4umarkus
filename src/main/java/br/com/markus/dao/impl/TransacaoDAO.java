package br.com.markus.dao.impl;

import br.com.markus.dao.ITransacaoDAO;
import br.com.markus.enuns.StatusSolicitacaoPagamentoEnum;
import br.com.markus.enuns.TipoBandeiraCartaoEnum;
import br.com.markus.exception.TransacaoInvalidaException;
import br.com.markus.message.ConstantesMessagem;
import br.com.markus.model.Transacao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe responsável por "persistir" solicitações de pagamento
 *
 * @author Markus Kopinits
 */
@Component
public class TransacaoDAO implements ITransacaoDAO {
    public static final String NSU_PADRAO = "123445566";
    public static final String NSU_PADRAO_CANCELAMENTO = "78958123";
    public static final String CODIGO_AUTORIZACAO_PADRAO = "998766541";
    public static final String CODIGO_AUTORIZACAO_PADRAO_CANCELAMENTO = "999999145";
    public static final String CARTAO_NEGADO = "999999999999";
    public static final String CARTAO_SEM_SALDO = "888888888888";

    private static Set<Transacao> listaTransacaoBrandX = new HashSet<Transacao>();
    private static Set<Transacao> listaTransacaoAssincronaBrandX = new HashSet<Transacao>();

    private static Set<Transacao> listaTransacaoBrandY = new HashSet<Transacao>();
    private static Set<Transacao> listaTransacaoAssincronaBrandY = new HashSet<Transacao>();

    public TransacaoDAO() {
    }

    public Transacao solicitarAutorizacao(Transacao transacao, boolean sincrona) throws TransacaoInvalidaException {
        Set<Transacao> fonteDeDados = recuperaFonteDeDados(transacao, sincrona);
        if (fonteDeDados.contains(transacao)) {
            throw new TransacaoInvalidaException(ConstantesMessagem.TRANSACAO_JA_EXISTENTE);
        } else {
            if (isTransacaoAutorizada(transacao)) {
                fonteDeDados.add(transacao);
            }
        }
        return transacao;
    }

    /**
     * Cancela uma transação
     * Define o status da transação conforme lógica interna, pois não é possível acessar os serviços reais.
     * Caso o cartão utilizado na tranbsação seja o cartao CARTAO_NEGADO, a transação será negada.
     * Caso o cartão utilizado na tranbsação seja o cartao CARTAO_SEM_SALDO, a transação será negada por falta de saldo.
     * Qualquer outro número de cartão, a transação será aprovada
     *
     * @param transacao transação que será cancelada
     * @return transação com o status atualizado
     * @throws TransacaoInvalidaException
     */
    public Transacao cancelarTransacao(Transacao transacao) throws TransacaoInvalidaException {
        Set<Transacao> fonteDeDados = recuperaFonteDeDados(transacao, true);
        if (!fonteDeDados.contains(transacao)) {
            throw new TransacaoInvalidaException(ConstantesMessagem.TRANSACAO_INEXISTENTE);
        } else {
            fonteDeDados.remove(transacao);
            transacao.getDadosPedido().setNsu(NSU_PADRAO_CANCELAMENTO);
            transacao.getDadosPedido().setCodigoAutorizacao(CODIGO_AUTORIZACAO_PADRAO_CANCELAMENTO);
            if (transacao.getDadosPedido().getNumeroCartao().equals(CARTAO_NEGADO)) {
                transacao.getDadosPedido().setStatusCancelamento(StatusSolicitacaoPagamentoEnum.NEGADO_BRAND_X);
            } else {
                transacao.getDadosPedido().setStatusCancelamento(StatusSolicitacaoPagamentoEnum.AUTORIZADO);
            }
            fonteDeDados.add(transacao);
        }
        return transacao;
    }


    public ArrayList<Transacao> consultarTransacao(List<Transacao> transacaoList) {
        ArrayList<Transacao> retorno = new ArrayList<Transacao>();
        if (!transacaoList.isEmpty()) {
            Set<Transacao> fonteDeDados = recuperaFonteDeDados(transacaoList.get(0), true);
            fonteDeDados.addAll(recuperaFonteDeDados(transacaoList.get(0), false));
            ArrayList<Transacao> todasTransacoesList = new ArrayList<Transacao>(fonteDeDados);
            for (Transacao transacao : transacaoList) {
                if (todasTransacoesList.contains(transacao)) {
                    retorno.add(todasTransacoesList.get(todasTransacoesList.indexOf(transacao)));
                }
            }
        }
        return retorno;
    }

    /**
     * Método que verifica se existem transações assíncronas para realizar atentativa de autorizar
     */
    public void processarTransacoesAssincronas() {
        processaTransacoes(listaTransacaoAssincronaBrandX);
        processaTransacoes(listaTransacaoAssincronaBrandY);
    }

    private void processaTransacoes(Set<Transacao> fonteDeDados) {
        for (Transacao transacao : fonteDeDados) {
            try {
                solicitarAutorizacao(transacao, true);
                fonteDeDados.remove(transacao);
            } catch (TransacaoInvalidaException e) {
                System.out.println("Não foi possível processar a transação " + transacao.getTid());
                System.out.println("motivo: " + e.getMessage());
            }
        }
    }

    /**
     * Define o status da transação conforme lógica interna, pois não é possível acessar os serviços reais.
     * Caso o cartão utilizado na tranbsação seja o cartao CARTAO_NEGADO, a transação será negada.
     * Caso o cartão utilizado na tranbsação seja o cartao CARTAO_SEM_SALDO, a transação será negada por falta de saldo.
     * Qualquer outro número de cartão, a transação será aprovada
     */
    private boolean isTransacaoAutorizada(Transacao transacao) {
        boolean autorizado = false;
        StatusSolicitacaoPagamentoEnum status;
        if (transacao.getDadosPedido().getNumeroCartao().equals(CARTAO_NEGADO)) {
            status = defineNegadoPelaBrandeira(transacao);
        } else if (transacao.getDadosPedido().getNumeroCartao().equals(CARTAO_SEM_SALDO)) {
            status = defineSemSaldoPelaBrandeira(transacao);
        } else {
            autorizado = true;
            status = StatusSolicitacaoPagamentoEnum.AUTORIZADO;
            transacao.getDadosPedido().setNsu(NSU_PADRAO);
            transacao.getDadosPedido().setCodigoAutorizacao(CODIGO_AUTORIZACAO_PADRAO);
        }
        transacao.getDadosPedido().setStatusPagamento(status);
        return autorizado;
    }

    private StatusSolicitacaoPagamentoEnum defineNegadoPelaBrandeira(Transacao transacao) {
        StatusSolicitacaoPagamentoEnum status;
        if (transacao.getTipoBandeiraCartaoEnum().equals(TipoBandeiraCartaoEnum.JSON)) {
            status = StatusSolicitacaoPagamentoEnum.NEGADO_BRAND_Y;
        } else {
            status = StatusSolicitacaoPagamentoEnum.NEGADO_BRAND_X;
        }
        return status;
    }

    private StatusSolicitacaoPagamentoEnum defineSemSaldoPelaBrandeira(Transacao transacao) {
        StatusSolicitacaoPagamentoEnum status;
        if (transacao.getTipoBandeiraCartaoEnum().equals(TipoBandeiraCartaoEnum.JSON)) {
            status = StatusSolicitacaoPagamentoEnum.SALDO_INSUFICIENTE_BRAND_Y;
        } else {
            status = StatusSolicitacaoPagamentoEnum.SALDO_INSUFICIENTE_BRAND_X;
        }
        return status;
    }


    private Set<Transacao> recuperaFonteDeDados(Transacao transacao, boolean sincrona) {
        if (transacao.getTipoBandeiraCartaoEnum().equals(TipoBandeiraCartaoEnum.JSON)) {
            return sincrona ? listaTransacaoBrandY : listaTransacaoAssincronaBrandY;
        } else {
            return sincrona ? listaTransacaoBrandX : listaTransacaoAssincronaBrandX;
        }
    }
}
