package br.com.markus.business;


import br.com.markus.enuns.TipoPagamentoEnum;
import br.com.markus.exception.TransacaoInvalidaException;
import br.com.markus.message.ConstantesMessagem;
import br.com.markus.model.DadosPedido;
import br.com.markus.model.FormaPagamento;
import br.com.markus.model.Transacao;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * Classe responsável por realizar os testes da classe TransacaoBusiness
 *
 * @author Markus Kopinits
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:testContext.xml")
public class TransacaoBusinessTest {

    @Autowired
    private ITransacaoBusiness transacaoBusiness;

    @Test
    public void testTransacaoInvalida(){
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(new Transacao());
        assert  exceptions.size() == 3;
    }

    @Test
    public void testTransacaoInvalidaTodosCampos(){
        Transacao transacao = new Transacao();
        transacao.setDadosPedido(new DadosPedido());
        transacao.setFormaPagamento(new FormaPagamento());
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 7;
    }

    @Test
    public void testTransacaoInvalidaTIDNulo(){
        Transacao transacao = criarTransacaoValida();
        transacao.setTid(null);
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.TID_OBRIGATORIO);
    }

    @Test
    public void testTransacaoInvalidaTIDInvalido(){
        Transacao transacao = criarTransacaoValida();
        transacao.setTid("123456");
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.TID_INVALIDO);
    }

    @Test
    public void testTransacaoInvalidaDadosPagamento(){
        Transacao transacao = criarTransacaoValida();
        transacao.setDadosPedido(new DadosPedido());
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 4;
    }

    @Test
    public void testTransacaoInvalidaDadosPagamentoCartaoNulo(){
        Transacao transacao = criarTransacaoValida();
        transacao.getDadosPedido().setNumeroCartao(null);
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.NUMERO_CARTAO_OBRIGATORIO);
    }

    @Test
    public void testTransacaoInvalidaDadosPagamentoCartaoInvalido(){
        Transacao transacao = criarTransacaoValida();
        transacao.getDadosPedido().setNumeroCartao("1234456");
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.NUMERO_CARTAO_INVALIDO);
    }

    @Test
    public void testTransacaoInvalidaDadosPagamentoValor(){
        Transacao transacao = criarTransacaoValida();
        transacao.getDadosPedido().setValor(null);
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.VALOR_OBRIGATORIO);
    }

    @Test
    public void testTransacaoInvalidaDadosPagamentoDescricaoNulo(){
        Transacao transacao = criarTransacaoValida();
        transacao.getDadosPedido().setDescricao(null);
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.DESCRICAO_OBRIGATORIA);
    }

    @Test
    public void testTransacaoInvalidaDadosPagamentoDataNulo(){
        Transacao transacao = criarTransacaoValida();
        transacao.getDadosPedido().setData(null);
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.DATA_OBRIGATORIA);
    }

    @Test
    public void testTransacaoInvalidaFormaPagamento(){
        Transacao transacao = criarTransacaoValida();
        transacao.setFormaPagamento(new FormaPagamento());
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 2;
    }

    @Test
    public void testTransacaoInvalidaFormaPagamentoParcelasInvalida(){
        Transacao transacao = criarTransacaoValida();
        transacao.getFormaPagamento().setQtdParcelas(-1);
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.QUANTIDADE_DE_PARCELAS_INVALIDA);
    }

    @Test
    public void testTransacaoInvalidaFormaPagamentoTipoPagamentoNulo(){
        Transacao transacao = criarTransacaoValida();
        transacao.getFormaPagamento().setTipo(null);
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.TIPO_FORMA_DE_PAGAMENTO_OBRIGATORIO);
    }

    @Test
    public void testTransacaoInvalidaFormaPagamentoTipoPagamentoInvalido(){
        Transacao transacao = criarTransacaoValida();
        transacao.getFormaPagamento().setTipo("TipoInexistente");
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.TIPO_FORMA_DE_PAGAMENTO_INVALIDO);
    }

    @Test
    public void testTransacaoInvalidaFormaPagamentoParcelasNulo(){
        Transacao transacao = criarTransacaoValida();
        transacao.getFormaPagamento().setQtdParcelas(null);
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.size() == 1;
        assert exceptions.get(0).getMessage().equals(ConstantesMessagem.QUANTIDADE_DE_PARCELAS_OBRIGATORIO);
    }

    @Test
    public void testTransacaoValida(){
        Transacao transacao = criarTransacaoValida();
        ArrayList<TransacaoInvalidaException> exceptions = transacaoBusiness.validarTransacao(transacao);
        assert  exceptions.isEmpty();
    }

    private Transacao criarTransacaoValida() {
        Transacao transacao = new Transacao();
        transacao.setDadosPedido(new DadosPedido());
        transacao.setFormaPagamento(new FormaPagamento());
        transacao.setTid("10017348980735271001");
        transacao.getDadosPedido().setNumeroCartao("113000643688");
        transacao.getDadosPedido().setValor(new BigDecimal(10));
        transacao.getDadosPedido().setData(new DateTime("2011-12-05T16:01:28.655-02:00").toDate());
        transacao.getDadosPedido().setDescricao("Loja de Conveniência");
        transacao.getFormaPagamento().setTipo(TipoPagamentoEnum.AVISTA.getDescricao());
        transacao.getFormaPagamento().setQtdParcelas(1);
        return transacao;
    }
}
