package br.com.markus.facade;

import br.com.markus.enuns.TipoBandeiraCartaoEnum;

/**
 * Interface para TransacaoFacade
 */
public interface ITransacaoFacade {

    String solicitarPagamento(String transacaoString, TipoBandeiraCartaoEnum tipoBandeiraCartaoEnum) throws Exception;

    String solicitarPagamentoAssincrono(String transacaoString, TipoBandeiraCartaoEnum tipoBandeiraCartaoEnum) throws Exception;

    String cancelarPagamento(String transacaoString, TipoBandeiraCartaoEnum tipoBandeiraCartaoEnum) throws Exception;

    String consultarPagamentos(String transacaoString, TipoBandeiraCartaoEnum tipoBandeiraCartaoEnum) throws Exception;
}
