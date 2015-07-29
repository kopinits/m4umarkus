package br.com.markus.webservice;

import br.com.markus.facade.TransacaoFacadeTest;
import br.com.markus.message.ConstantesMessagem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Classe responsável por realizar os testes dos webservices
 *
 * @author Markus Kopinits
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:testContext.xml")
public class WebServicesTest{
    @Autowired
    private SolicitarPagamento solicitarPagamentoWS;

    @Test
    public void testRetornoSolicitacaoInvalidaJSON(){
        String resposta = solicitarPagamentoWS.solicitarPagamento(TransacaoFacadeTest.TEXTO_INVALIDO_ENTRADA);
        assert resposta.equals(ConstantesMessagem.ERRO_GENERICO);
    }

    @Test
    public void testRetornoSolicitacaoInvalidaXML(){
        String resposta = solicitarPagamentoWS.solicitarPagamento(TransacaoFacadeTest.TEXTO_INVALIDO_ENTRADA);
        assert resposta.equals(ConstantesMessagem.ERRO_GENERICO);
    }
}
