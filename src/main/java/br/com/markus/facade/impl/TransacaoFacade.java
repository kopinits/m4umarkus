package br.com.markus.facade.impl;

import br.com.markus.message.ConstantesMessagem;
import br.com.markus.business.ITransacaoBusiness;
import br.com.markus.converter.IConversorTransacao;
import br.com.markus.converter.impl.FabricaConversorTransacao;
import br.com.markus.enuns.TipoBandeiraCartaoEnum;
import br.com.markus.facade.ITransacaoFacade;
import br.com.markus.model.Transacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


/**
 * Classe responsável por ser a fachada para processar as solicitações de pagamento
 *
 * @author Markus Kopinits
 */
@Component
public class TransacaoFacade implements ITransacaoFacade {

    private ITransacaoBusiness transacaoBusiness;

    public TransacaoFacade() {
    }

    @Autowired
    public TransacaoFacade(ITransacaoBusiness transacaoBusiness) {
        this.transacaoBusiness = transacaoBusiness;
    }

    /**
     * Método responsável por realizar a solicitação de pagamento de forma síncrona
     *
     * @param transacaoString
     * @param tipoBandeiraCartaoEnum
     * @return resposta da solicitação no formato XML ou JSON
     * @throws Exception
     */
    public String solicitarPagamento(String transacaoString, TipoBandeiraCartaoEnum tipoBandeiraCartaoEnum) throws Exception {
        IConversorTransacao converter = new FabricaConversorTransacao().getConverter(tipoBandeiraCartaoEnum);
        Transacao transacao = converter.toTransacao(transacaoString);
        Transacao retornoTransacao = transacaoBusiness.solicitarPagamento(transacao);
        return converter.fromTransacao(retornoTransacao);
    }

    /**
     * Método responsável por realizar a solicitação de pagamento de forma asssíncrona
     *
     * @param transacaoString
     * @param tipoBandeiraCartaoEnum
     * @return resposta da solicitação no formato XML ou JSON
     * @throws Exception
     */
    public String solicitarPagamentoAssincrono(String transacaoString, TipoBandeiraCartaoEnum tipoBandeiraCartaoEnum) throws Exception {
        IConversorTransacao converter = new FabricaConversorTransacao().getConverter(tipoBandeiraCartaoEnum);
        Transacao transacao = converter.toTransacao(transacaoString);
        transacaoBusiness.solicitarPagamentoAssincrono(transacao);
        return converter.getMensagemServicoOff();
    }

    /**
     * Método responsável por realizar o cancelamento de uma solicitaçao de pagamento
     *
     * @param transacaoString
     * @param tipoBandeiraCartaoEnum
     * @return resposta da solicitação no formato XML ou JSON
     * @throws Exception
     */
    public String cancelarPagamento(String transacaoString, TipoBandeiraCartaoEnum tipoBandeiraCartaoEnum) throws Exception {
        IConversorTransacao converter = new FabricaConversorTransacao().getConverter(tipoBandeiraCartaoEnum);
        Transacao transacao = converter.toTransacao(transacaoString);
        Transacao retornoTransacao = transacaoBusiness.cancelarPagamento(transacao);
        return converter.fromTransacao(retornoTransacao);
    }

    /**
     * Método responsável por realizar a consulta de solicitações de pagamentos existentes, síncronas e assíncronas
     *
     * @param transacaoString
     * @param tipoBandeiraCartaoEnum
     * @return resposta da solicitação no formato XML ou JSON
     * @throws Exception
     */
    public String consultarPagamentos(String transacaoString, TipoBandeiraCartaoEnum tipoBandeiraCartaoEnum) throws Exception {
        IConversorTransacao converter = new FabricaConversorTransacao().getConverter(tipoBandeiraCartaoEnum);
        ArrayList<Transacao> transacaoArrayList = converter.toTransacaoConsulta(transacaoString);
        ArrayList<Transacao> transacaoList = transacaoBusiness.consultarPagamentos(transacaoArrayList);
        if (!transacaoList.isEmpty()) {
            return converter.fromTransacao(transacaoList);
        } else {
            return ConstantesMessagem.SEM_REGISTROS;
        }
    }
}
