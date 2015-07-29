package br.com.markus.webservice;

import br.com.markus.facade.ITransacaoFacade;
import br.com.markus.webservice.util.TransacaoExceptionHandler;
import br.com.markus.webservice.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "ws")
public class SolicitarPagamento {
    private ITransacaoFacade transacaoFacade;

    @Autowired
    public SolicitarPagamento(ITransacaoFacade transacaoFacade) {
        this.transacaoFacade = transacaoFacade;
    }

    @ResponseBody
    @RequestMapping(value = "solicitar", method = RequestMethod.POST)
    public String solicitarPagamento(@RequestBody String requisicaoString) {
        try {
            return transacaoFacade.solicitarPagamentoAssincrono(requisicaoString, Utils.descobrirTipoRequisicao(requisicaoString));
        } catch (Exception e) {
            return TransacaoExceptionHandler.retornaExcecao(e);
        }
    }
}
