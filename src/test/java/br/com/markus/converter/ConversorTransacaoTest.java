package br.com.markus.converter;

import br.com.markus.enuns.TipoBandeiraCartaoEnum;
import br.com.markus.enuns.TipoPagamentoEnum;
import br.com.markus.model.DadosPedido;
import br.com.markus.model.FormaPagamento;
import br.com.markus.model.Transacao;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Classe responsável por realizar os testes de conversão das entrada JSON e XML para o bean Transacao e vice-versa
 *
 * @author Markus Kopinits
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:testContext.xml")
public class ConversorTransacaoTest{


    @Autowired
    private IFabricaConversorTransacao conversorTransacao;
    private Transacao transacaoBase;

    @Before
    public void setUp() {
        transacaoBase = new Transacao();
        transacaoBase.setDadosPedido(new DadosPedido());
        transacaoBase.setFormaPagamento(new FormaPagamento());
        transacaoBase.setTid("10017348980735271001");
        transacaoBase.getDadosPedido().setNumeroCartao("1130006436");
        transacaoBase.getDadosPedido().setValor(new BigDecimal(10));
        transacaoBase.getDadosPedido().setData(new DateTime("2011-12-05T16:01:28.655-02:00").toDate());
        transacaoBase.getDadosPedido().setDescricao("Loja de Conveniência");
        transacaoBase.getFormaPagamento().setTipo(TipoPagamentoEnum.AVISTA.getDescricao());
        transacaoBase.getFormaPagamento().setQtdParcelas(1);
    }

    /**
     * Método responsável por testar a conversão da transação no formato JSON para o bean Transacao
     */
    @Test
    public void testJSONToTransacao() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.JSON);
        String jsonTransacao = "{" +
                "\"transacao\":{" +
                "\"tid\":\"10017348980735271001\"," +
                "\"dados-pedido\":{" +
                "\"numero_cartao\":\"1130006436\"," +
                "\"valor\":\"1000\"," +
                "\"data-hora\":\"2011-12-05T16:01:28.655-02:00\"," +
                "\"descricao\":\"Loja de Conveniência\"" +
                "}," +
                "\"forma-pagamento\":{" +
                "\"tipo\":\"AVISTA\"," +
                "\"quantidade\":\"1\"" +
                "}}}";
        validarTransacaoConvertida(converter.toTransacao(jsonTransacao));
    }

    /**
     * Método responsável por testar a conversão do bean Transacao para Strign no formato JSON
     */
    @Test
    public void testTransacaotoJSON() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.JSON);
        String jsonConvertido = converter.fromTransacao(transacaoBase);
        validarTransacaoConvertida(converter.toTransacao(jsonConvertido));
    }

    /**
     * Método responsável por testar a conversão da transação no formato XML para o bean Transacao
     */
    @Test
    public void testXMLToTransacao() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.XML);
        String xmlTransacao = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<transacao>" +
                "<tid>10017348980735271001</tid>" +
                "<dados-pedido>" +
                "<numero_cartao>1130006436</numero_cartao>" +
                "<valor>1000</valor>" +
                "<data-hora>2011-12-05T16:01:28.655-02:00</data-hora>" +
                "<descricao>Loja de Conveniência</descricao> " +
                "</dados-pedido>" +
                "<forma-pagamento>" +
                "<modalidade>AVISTA</modalidade>" +
                "<parcelas>1</parcelas> " +
                "</forma-pagamento>" +
                "</transacao>";
        validarTransacaoConvertida(converter.toTransacao(xmlTransacao));
    }

    /**
     * Método responsável por testar a conversão do bean Transacao para Strign no formato XML
     */
    @Test
    public void testTransacaoToXML() throws Exception {
        IConversorTransacao converter = conversorTransacao.getConverter(TipoBandeiraCartaoEnum.XML);
        String xmlConvertido = converter.fromTransacao(transacaoBase);
        validarTransacaoConvertida(converter.toTransacao(xmlConvertido));
    }

    private void validarTransacaoConvertida(Transacao transacaoConvertida) {
        assert transacaoBase.getTid().equals(transacaoConvertida.getTid());
        assert transacaoBase.getDadosPedido().getNumeroCartao().equals(transacaoConvertida.getDadosPedido().getNumeroCartao());
        assert transacaoBase.getDadosPedido().getValor().compareTo(transacaoConvertida.getDadosPedido().getValor()) == 0;
        assert transacaoBase.getDadosPedido().getData().equals(transacaoConvertida.getDadosPedido().getData());
        assert transacaoBase.getDadosPedido().getDescricao().equals(transacaoConvertida.getDadosPedido().getDescricao());
        assert transacaoBase.getFormaPagamento().getTipo().equals(transacaoConvertida.getFormaPagamento().getTipo());
        assert transacaoBase.getFormaPagamento().getQtdParcelas().equals(transacaoConvertida.getFormaPagamento().getQtdParcelas());
    }


}
