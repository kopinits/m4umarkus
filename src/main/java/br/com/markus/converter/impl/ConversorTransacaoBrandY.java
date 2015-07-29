package br.com.markus.converter.impl;

import br.com.markus.message.ConstantesMessagem;
import br.com.markus.converter.IConversorTransacao;
import br.com.markus.enuns.StatusSolicitacaoPagamentoEnum;
import br.com.markus.enuns.TipoBandeiraCartaoEnum;
import br.com.markus.model.DadosPedido;
import br.com.markus.model.FormaPagamento;
import br.com.markus.model.Transacao;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por converter a solicitação em formato JSON para o bean Transacao
 *
 * @author Markus Kopinits
 */
public class ConversorTransacaoBrandY implements IConversorTransacao {
    private static final String TID = "tid";
    private static final String TRANSACAO = "transacao";
    private static final String FORMA_PAGAMENTO = "forma-pagamento";
    private static final String TIPO_PAGAMENTO = "tipo";
    private static final String QUANTIDADE_PAGAMENTO = "quantidade";
    private static final String DADOS_PEDIDO = "dados-pedido";
    private static final String NUMERO_CARTAO = "numero_cartao";
    private static final String DATA_HORA = "data-hora";
    private static final String DESCRICAO = "descricao";
    private static final String VALOR = "valor";
    private static final String NSU = "nsu";
    private static final String CODIGO_AUTORIZACAO = "codigoAutorizacao";
    private static final String STATUS = "status";
    private static final String STATUS_CANCELAMENTO = "statusCancelamento";

    /**
     * Método responsável por converter a String, no formato JSON, da transação para o bean Transacao
     *
     * @param jsonString json da transacao
     * @return Transacao
     */
    @Override
    public Transacao toTransacao(String jsonString) throws Exception {
        if (StringUtils.isNotBlank(jsonString)) {
            Transacao transacao = criaBean();
            JSONObject jsonObject = preencheDadosTransacao(jsonString, transacao);
            preencheDadosPedido(jsonObject, transacao);
            preencheFormaPagamento(jsonObject, transacao);
            transacao.setTipoBandeiraCartaoEnum(TipoBandeiraCartaoEnum.JSON);
            return transacao;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Transacao> toTransacaoConsulta(String jsonString) throws Exception{
        ArrayList<Transacao> transacaoArrayList = new ArrayList<Transacao>();
        if (StringUtils.isNotBlank(jsonString)) {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int p = 0; p < jsonArray.length(); p++){
                transacaoArrayList.add(toTransacao(jsonArray.getString(p)));
            }
            return transacaoArrayList;
        } else {
            return null;
        }
    }

    /**
     * Método responsável por converter o bean Transacao para a String no formato JSON
     *
     * @param transacao transação que será convertida
     * @return String
     */
    public String fromTransacao(Transacao transacao) throws Exception {
        if (transacao != null) {
            //multiplica-se por 100 para o valor 10,00 ser convertido em 1000
            BigDecimal valorPedido = transacao.getDadosPedido().getValor();
            transacao.getDadosPedido().setValor(valorPedido.multiply(BigDecimal.valueOf(100)));
            JSONObject jsonObject = new JSONObject(transacao.toString());
            //voltando o valor original
            transacao.getDadosPedido().setValor(valorPedido);
            return jsonObject.toString();
        } else {
            return "";
        }
    }

    /**
     * Método responsável por converter uma lista de beans Transacao para a String no formato JSON
     *
     * @param transacaoList lista de transações que serão convertidas
     * @return String
     */
    @Override
    public String fromTransacao(List<Transacao> transacaoList) throws Exception {
        StringBuilder retorno = new StringBuilder("[");
        for (Transacao transacao : transacaoList) {
            retorno.append(fromTransacao(transacao));
            retorno.append(",");
        }
        retorno.append("]");
        return retorno.toString();
    }

    @Override
    public String getMensagemServicoOff() {
        return ConstantesMessagem.MSG_SERVICO_OFFLINE_JSON;
    }

    /*
         *  Método que preenche o bean Transacao com os dados do JSON
         */
    private JSONObject preencheDadosTransacao(String jsonString, Transacao bean) throws JSONException {
        if (StringUtils.isNotBlank(jsonString)) {
            JSONObject transacao = (JSONObject) new JSONObject(jsonString).get(TRANSACAO);
            bean.setTid(transacao.get(TID).toString());
            return transacao;
        } else {
            return new JSONObject();
        }
    }

    /*
     * Método que cria um bean Transacao
     */
    private Transacao criaBean() {
        Transacao bean = new Transacao();
        bean.setDadosPedido(new DadosPedido());
        bean.setFormaPagamento(new FormaPagamento());
        return bean;
    }

    /*
     * Método que preenche a forma de Pagamento do bean Transacao com os dados do JSON
     */
    private void preencheFormaPagamento(JSONObject transacao, Transacao bean) throws JSONException {
        if (transacao.has(FORMA_PAGAMENTO)) {
            JSONObject pagamento = (JSONObject) transacao.get(FORMA_PAGAMENTO);
            bean.getFormaPagamento().setTipo(pagamento.get(TIPO_PAGAMENTO).toString());
            Integer of = Integer.valueOf(pagamento.get(QUANTIDADE_PAGAMENTO).toString());
            bean.getFormaPagamento().setQtdParcelas(of);
        }
    }

    /*
     * Método que preenche os Dados do Pedido do bean Transacao com os dados do JSON
     */
    private void preencheDadosPedido(JSONObject transacao, Transacao bean) throws JSONException {
        if (transacao.has(DADOS_PEDIDO)) {
            JSONObject dadosPedido = (JSONObject) transacao.get(DADOS_PEDIDO);
            bean.getDadosPedido().setNumeroCartao(dadosPedido.get(NUMERO_CARTAO).toString());
            BigDecimal bigDecimal = new BigDecimal(dadosPedido.get(VALOR).toString()).divide(BigDecimal.valueOf(100));
            bean.getDadosPedido().setValor(bigDecimal);
            DateTime dateTime = new DateTime(dadosPedido.get(DATA_HORA));
            bean.getDadosPedido().setData(dateTime.toDate());
            bean.getDadosPedido().setDescricao(dadosPedido.get(DESCRICAO).toString());
            if (dadosPedido.has(NSU)) {
                bean.getDadosPedido().setNsu(dadosPedido.get(NSU).toString());
            }
            if (dadosPedido.has(CODIGO_AUTORIZACAO)) {
                bean.getDadosPedido().setCodigoAutorizacao(dadosPedido.get(CODIGO_AUTORIZACAO).toString());
            }
            if (dadosPedido.has(STATUS)) {
                bean.getDadosPedido().setStatusPagamento(StatusSolicitacaoPagamentoEnum.from(dadosPedido.get(STATUS).toString()));
            }
            if (dadosPedido.has(STATUS_CANCELAMENTO)) {
                bean.getDadosPedido().setStatusCancelamento(StatusSolicitacaoPagamentoEnum.from(dadosPedido.get(STATUS_CANCELAMENTO).toString()));
            }
        }
    }
}
