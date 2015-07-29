package br.com.markus.facade;


import br.com.markus.converter.IConversorTransacao;
import br.com.markus.converter.IFabricaConversorTransacao;
import br.com.markus.dao.impl.TransacaoDAO;
import br.com.markus.enuns.StatusSolicitacaoPagamentoEnum;
import br.com.markus.enuns.TipoBandeiraCartaoEnum;
import br.com.markus.enuns.TipoPagamentoEnum;
import br.com.markus.exception.TransacaoInvalidaException;
import br.com.markus.message.ConstantesMessagem;
import br.com.markus.model.DadosPedido;
import br.com.markus.model.FormaPagamento;
import br.com.markus.model.Transacao;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Classe responsável por realizar os testes da classe TransacaoFacade
 *
 * @author Markus Kopinits
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:testContext.xml")
public class TransacaoFacadeTest {
    public static final String TEXTO_INVALIDO_ENTRADA = "TEXTO INVALIDO ENTRADA";
    @Autowired
    private ITransacaoFacade transacaoFacade;

    @Autowired
    private IFabricaConversorTransacao conversorTransacao;

    /*
     ************************************************
     **** TESTES PARA O FORMATO JSON (Brand Y)   ****
     ************************************************
     */

    @Test
    public void testFormatoJSONInvalido() throws Exception {
        try {
            transacaoFacade.solicitarPagamento(TEXTO_INVALIDO_ENTRADA, TipoBandeiraCartaoEnum.JSON);
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    public void testTransacaoJaExistenteJSON() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.JSON);
        String resposta = criarEPersistirSolicitacaoPagamento(converter, true, TipoBandeiraCartaoEnum.JSON);
        try{
            transacaoFacade.solicitarPagamento(resposta, TipoBandeiraCartaoEnum.JSON);
        }catch (TransacaoInvalidaException te) {
            assert te.getMessage().equals(ConstantesMessagem.TRANSACAO_JA_EXISTENTE);
        }
    }

    @Test
    public void testTransacaoAprovadaJSON() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.JSON);
        String resposta = criarEPersistirSolicitacaoPagamento(converter, true, TipoBandeiraCartaoEnum.JSON);
        Transacao transacaoResposta = converter.toTransacao(resposta);
        assert transacaoResposta.getDadosPedido().getStatusPagamento().equals(StatusSolicitacaoPagamentoEnum.AUTORIZADO);
        assert StringUtils.isNotBlank(transacaoResposta.getDadosPedido().getNsu());
        assert StringUtils.isNotBlank(transacaoResposta.getDadosPedido().getCodigoAutorizacao());
    }

    @Test
    public void testTransacaoAprovadaJSONAssincrona() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.JSON);
        String resposta = criarEPersistirSolicitacaoPagamento(converter, false, TipoBandeiraCartaoEnum.JSON);
        assert ConstantesMessagem.MSG_SERVICO_OFFLINE_JSON.equals(resposta);
    }

    @Test
    public void testTransacaoNegadaJSON() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.JSON);
        Transacao transacao = criarTransacaoValida();
        transacao.getDadosPedido().setNumeroCartao(TransacaoDAO.CARTAO_NEGADO);
        String stringTransacao = converter.fromTransacao(transacao);
        String resposta = transacaoFacade.solicitarPagamento(stringTransacao, TipoBandeiraCartaoEnum.JSON);
        Transacao transacaoResposta = converter.toTransacao(resposta);
        assert transacaoResposta.getDadosPedido().getStatusPagamento().equals(StatusSolicitacaoPagamentoEnum.NEGADO_BRAND_Y);
        assert transacaoResposta.getDadosPedido().getStatusCancelamento() == null;
        assert StringUtils.isBlank(transacaoResposta.getDadosPedido().getNsu());
        assert StringUtils.isBlank(transacaoResposta.getDadosPedido().getCodigoAutorizacao());
    }

    @Test
    public void testTransacaoSemSaldoJSON() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.JSON);
        Transacao transacao = criarTransacaoValida();
        transacao.getDadosPedido().setNumeroCartao(TransacaoDAO.CARTAO_SEM_SALDO);
        String stringTransacao = converter.fromTransacao(transacao);
        String resposta = transacaoFacade.solicitarPagamento(stringTransacao, TipoBandeiraCartaoEnum.JSON);
        Transacao transacaoResposta = converter.toTransacao(resposta);
        assert transacaoResposta.getDadosPedido().getStatusPagamento().equals(StatusSolicitacaoPagamentoEnum.SALDO_INSUFICIENTE_BRAND_Y);
        assert transacaoResposta.getDadosPedido().getStatusCancelamento() == null;
        assert StringUtils.isBlank(transacaoResposta.getDadosPedido().getNsu());
        assert StringUtils.isBlank(transacaoResposta.getDadosPedido().getCodigoAutorizacao());
    }

    @Test
    public void testCancelarTransacaoJSONSucesso() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.JSON);
        String stringTransacao = converter.fromTransacao(criarTransacaoValida());
        transacaoFacade.solicitarPagamento(stringTransacao, TipoBandeiraCartaoEnum.JSON);
        String resposta = transacaoFacade.cancelarPagamento(stringTransacao, TipoBandeiraCartaoEnum.JSON);
        Transacao transacaoResposta = converter.toTransacao(resposta);
        assert transacaoResposta.getDadosPedido().getStatusCancelamento().equals(StatusSolicitacaoPagamentoEnum.AUTORIZADO);
        assert transacaoResposta.getDadosPedido().getStatusPagamento() == null;
        assert StringUtils.isNotBlank(transacaoResposta.getDadosPedido().getNsu());
        assert StringUtils.isNotBlank(transacaoResposta.getDadosPedido().getCodigoAutorizacao());
    }

    @Test
    public void testConsultarSolicitacoesJSON() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.JSON);
        String resposta = criarEPersistirSolicitacaoPagamento(converter, true, TipoBandeiraCartaoEnum.JSON);
        criarEPersistirSolicitacaoPagamento(converter, true, TipoBandeiraCartaoEnum.JSON);
        criarEPersistirSolicitacaoPagamento(converter, false, TipoBandeiraCartaoEnum.JSON);
        criarEPersistirSolicitacaoPagamento(converter, true, TipoBandeiraCartaoEnum.JSON);
        String listaConsulta = transacaoFacade.consultarPagamentos("[" + resposta + "]", TipoBandeiraCartaoEnum.JSON);
        assert StringUtils.isNotEmpty(listaConsulta);
    }


    /*
     ************************************************
     **** TESTES PARA O FORMATO XML (Brand X)    ****
     ************************************************
     */
    @Test
    public void testFormatoXMLInvalido() throws Exception {
        try {
            transacaoFacade.solicitarPagamento(TEXTO_INVALIDO_ENTRADA, TipoBandeiraCartaoEnum.XML);
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    public void testTransacaoJaExistenteXML() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.XML);
        String resposta = criarEPersistirSolicitacaoPagamento(converter, true, TipoBandeiraCartaoEnum.XML);
        try{
            transacaoFacade.solicitarPagamento(resposta, TipoBandeiraCartaoEnum.XML);
        }catch (TransacaoInvalidaException te) {
            assert te.getMessage().equals(ConstantesMessagem.TRANSACAO_JA_EXISTENTE);
        }
    }

    @Test
    public void testTransacaoAprovadaXML() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.XML);
        String resposta = criarEPersistirSolicitacaoPagamento(converter, true, TipoBandeiraCartaoEnum.XML);
        Transacao transacaoResposta = converter.toTransacao(resposta);
        assert transacaoResposta.getDadosPedido().getStatusPagamento().equals(StatusSolicitacaoPagamentoEnum.AUTORIZADO);
        assert transacaoResposta.getDadosPedido().getStatusCancelamento() == null;
        assert StringUtils.isNotBlank(transacaoResposta.getDadosPedido().getNsu());
        assert StringUtils.isNotBlank(transacaoResposta.getDadosPedido().getCodigoAutorizacao());
    }

    @Test
    public void testTransacaoAprovadaXMLAssincrona() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.XML);
        String resposta = criarEPersistirSolicitacaoPagamento(converter, false, TipoBandeiraCartaoEnum.XML);
        assert ConstantesMessagem.MSG_SERVICO_OFFLINE_XML.equals(resposta);
    }

    @Test
    public void testTransacaoNegadaXML() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.XML);
        Transacao transacao = criarTransacaoValida();
        transacao.getDadosPedido().setNumeroCartao(TransacaoDAO.CARTAO_NEGADO);
        String stringTransacao = converter.fromTransacao(transacao);
        String resposta = transacaoFacade.solicitarPagamento(stringTransacao, TipoBandeiraCartaoEnum.XML);
        Transacao transacaoResposta = converter.toTransacao(resposta);
        assert transacaoResposta.getDadosPedido().getStatusPagamento().equals(StatusSolicitacaoPagamentoEnum.NEGADO_BRAND_X);
        assert transacaoResposta.getDadosPedido().getStatusCancelamento() == null;
        assert StringUtils.isBlank(transacaoResposta.getDadosPedido().getNsu());
        assert StringUtils.isBlank(transacaoResposta.getDadosPedido().getCodigoAutorizacao());
    }

    @Test
    public void testTransacaoSemSaldoXML() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.XML);
        Transacao transacao = criarTransacaoValida();
        transacao.getDadosPedido().setNumeroCartao(TransacaoDAO.CARTAO_SEM_SALDO);
        String stringTransacao = converter.fromTransacao(transacao);
        String resposta = transacaoFacade.solicitarPagamento(stringTransacao, TipoBandeiraCartaoEnum.XML);
        Transacao transacaoResposta = converter.toTransacao(resposta);
        assert transacaoResposta.getDadosPedido().getStatusPagamento().equals(StatusSolicitacaoPagamentoEnum.SALDO_INSUFICIENTE_BRAND_X);
        assert transacaoResposta.getDadosPedido().getStatusCancelamento() == null;
        assert StringUtils.isBlank(transacaoResposta.getDadosPedido().getNsu());
        assert StringUtils.isBlank(transacaoResposta.getDadosPedido().getCodigoAutorizacao());
    }

    @Test
    public void testCancelarTransacaoXMLSucesso() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.XML);
        String stringTransacao = converter.fromTransacao(criarTransacaoValida());
        transacaoFacade.solicitarPagamento(stringTransacao, TipoBandeiraCartaoEnum.XML);
        String resposta = transacaoFacade.cancelarPagamento(stringTransacao, TipoBandeiraCartaoEnum.XML);
        Transacao transacaoResposta = converter.toTransacao(resposta);
        assert transacaoResposta.getDadosPedido().getStatusCancelamento().equals(StatusSolicitacaoPagamentoEnum.AUTORIZADO);
        assert transacaoResposta.getDadosPedido().getStatusPagamento() == null;
        assert StringUtils.isNotBlank(transacaoResposta.getDadosPedido().getNsu());
        assert StringUtils.isNotBlank(transacaoResposta.getDadosPedido().getCodigoAutorizacao());
    }

    @Test
    public void testConsultarSolicitacoesXML() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.XML);
        String stringResposta = criarEPersistirSolicitacaoPagamento(converter, true, TipoBandeiraCartaoEnum.XML);
        criarEPersistirSolicitacaoPagamento(converter, true, TipoBandeiraCartaoEnum.XML);
        criarEPersistirSolicitacaoPagamento(converter, false, TipoBandeiraCartaoEnum.XML);
        criarEPersistirSolicitacaoPagamento(converter, true, TipoBandeiraCartaoEnum.XML);
        Transacao transacao = converter.toTransacao(stringResposta);
        stringResposta = "<list><transacao><tid>" + transacao.getTid() + "</tid></transacao></list>";
        String listaConsulta = transacaoFacade.consultarPagamentos(stringResposta, TipoBandeiraCartaoEnum.XML);
        assert StringUtils.isNotBlank(listaConsulta);
    }

    /*
     ************************************************
     *********     MÉTODOS AUXILIARES        ********
     ************************************************
     */

    private Transacao criarTransacaoValida() {
        Transacao transacao = new Transacao();
        transacao.setDadosPedido(new DadosPedido());
        transacao.setFormaPagamento(new FormaPagamento());
        transacao.setTid(criarTIDAleatorio());
        transacao.getDadosPedido().setNumeroCartao("113000643611");
        transacao.getDadosPedido().setValor(new BigDecimal(10.89));
        transacao.getDadosPedido().setData(new DateTime("2011-12-05T16:01:28.655-02:00").toDate());
        transacao.getDadosPedido().setDescricao("Loja de Conveniencia");
        transacao.getFormaPagamento().setTipo(TipoPagamentoEnum.AVISTA.getDescricao());
        transacao.getFormaPagamento().setQtdParcelas(1);
        return transacao;
    }

    private String criarTIDAleatorio() {
        String tid = String.valueOf(Math.random() * 1000000);
        tid = tid.replace(".", "");
        tid = StringUtils.leftPad(tid, 20, "0");
        return tid;
    }

    private String criarEPersistirSolicitacaoPagamento(IConversorTransacao converter, boolean sincrono, TipoBandeiraCartaoEnum bandeira) throws Exception {
        String stringTransacao = converter.fromTransacao(criarTransacaoValida());
        if (sincrono) {
            return transacaoFacade.solicitarPagamento(stringTransacao, bandeira);
        } else {
            return transacaoFacade.solicitarPagamentoAssincrono(stringTransacao, bandeira);
        }
    }
}
