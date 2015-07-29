package br.com.markus.business.impl;

import br.com.markus.message.ConstantesMessagem;
import br.com.markus.business.ITransacaoBusiness;
import br.com.markus.dao.ITransacaoDAO;
import br.com.markus.enuns.TipoPagamentoEnum;
import br.com.markus.exception.MultipleTransacaoInvalidaException;
import br.com.markus.exception.TransacaoInvalidaException;
import br.com.markus.model.Transacao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela lógica de negócio para processar as solicitações de pagamento
 *
 * @author Markus Kopinits
 */
@Component
public class TransacaoBusiness implements ITransacaoBusiness {

    private ITransacaoDAO transacaoDAO;

    public TransacaoBusiness() {
    }

    @Autowired
    public TransacaoBusiness(ITransacaoDAO transacaoDAO) {
        this.transacaoDAO = transacaoDAO;
    }

    public Transacao solicitarPagamento(Transacao transacao) throws TransacaoInvalidaException {
        ArrayList<TransacaoInvalidaException> exceptions = validarTransacao(transacao);
        validarExcecoes(exceptions);
        return transacaoDAO.solicitarAutorizacao(transacao, true);
    }

    public void solicitarPagamentoAssincrono(Transacao transacao) throws TransacaoInvalidaException {
        ArrayList<TransacaoInvalidaException> exceptions = validarTransacao(transacao);
        validarExcecoes(exceptions);
        transacaoDAO.solicitarAutorizacao(transacao, false);
    }

    public Transacao cancelarPagamento(Transacao transacao) throws TransacaoInvalidaException {
        ArrayList<TransacaoInvalidaException> exceptions = validarTransacao(transacao);
        validarExcecoes(exceptions);
        return transacaoDAO.cancelarTransacao(transacao);
    }

    public ArrayList<Transacao> consultarPagamentos(List<Transacao> transacaoList) {
        return transacaoDAO.consultarTransacao(transacaoList);
    }

    public ArrayList<TransacaoInvalidaException> validarTransacao(Transacao transacao) {
        ArrayList<TransacaoInvalidaException> errors = new ArrayList<TransacaoInvalidaException>();
        validarTID(transacao, errors);
        validarDadosPedido(transacao, errors);
        validarFormaPagamento(transacao, errors);
        return errors;
    }


    /**
     * Método que verifica se existem transações assíncronas para realizar atentativa de autorizar
     */
    public void processarTransacoesAssincronas() {
        transacaoDAO.processarTransacoesAssincronas();
    }

    private void validarExcecoes(ArrayList<TransacaoInvalidaException> exceptions) {
        if (!exceptions.isEmpty()) {
            throw new MultipleTransacaoInvalidaException(exceptions);
        }
    }

    private void validarFormaPagamento(Transacao transacao, ArrayList<TransacaoInvalidaException> errors) {
        if (transacao.getFormaPagamento() == null) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.FORMA_DE_PAGAMENTO_OBRIGATORIA));
        } else {
            validarTipoFormaPagamento(transacao, errors);
            validarQuantidadeParcelas(transacao, errors);
        }
    }

    private void validarDadosPedido(Transacao transacao, ArrayList<TransacaoInvalidaException> errors) {
        if (transacao.getDadosPedido() == null) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.DADOS_DO_PEDIDO_OBRIGATORIO));
        } else {
            validarNumeroCartao(transacao, errors);
            validarValor(transacao, errors);
            validarDescricao(transacao, errors);
            validarData(transacao, errors);
        }
    }

    private void validarQuantidadeParcelas(Transacao transacao, ArrayList<TransacaoInvalidaException> errors) {
        if (transacao.getFormaPagamento().getQtdParcelas() == null) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.QUANTIDADE_DE_PARCELAS_OBRIGATORIO));
        }else if (transacao.getFormaPagamento().getQtdParcelas() <= 0){
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.QUANTIDADE_DE_PARCELAS_INVALIDA));
        }
    }

    private void validarTipoFormaPagamento(Transacao transacao, ArrayList<TransacaoInvalidaException> errors) {
        if (StringUtils.isEmpty(transacao.getFormaPagamento().getTipo())) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.TIPO_FORMA_DE_PAGAMENTO_OBRIGATORIO));
        } else if (TipoPagamentoEnum.from(transacao.getFormaPagamento().getTipo()) == null) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.TIPO_FORMA_DE_PAGAMENTO_INVALIDO));
        }
    }

    private void validarData(Transacao transacao, ArrayList<TransacaoInvalidaException> errors) {
        if (transacao.getDadosPedido().getData() == null) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.DATA_OBRIGATORIA));
        }
    }

    private void validarDescricao(Transacao transacao, ArrayList<TransacaoInvalidaException> errors) {
        if (StringUtils.isEmpty(transacao.getDadosPedido().getDescricao())) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.DESCRICAO_OBRIGATORIA));
        }
    }

    private void validarValor(Transacao transacao, ArrayList<TransacaoInvalidaException> errors) {
        if (transacao.getDadosPedido().getValor() == null) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.VALOR_OBRIGATORIO));
        }
    }

    private void validarNumeroCartao(Transacao transacao, ArrayList<TransacaoInvalidaException> errors) {
        String numeroCartao = transacao.getDadosPedido().getNumeroCartao();
        if (StringUtils.isBlank(numeroCartao)) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.NUMERO_CARTAO_OBRIGATORIO));
        }else if (numeroCartao.length() < 12) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.NUMERO_CARTAO_INVALIDO));
        }
    }

    private void validarTID(Transacao transacao, ArrayList<TransacaoInvalidaException> errors) {
        if (StringUtils.isEmpty(transacao.getTid())) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.TID_OBRIGATORIO));
        }
        if (StringUtils.isNotEmpty(transacao.getTid()) && transacao.getTid().length() < 20) {
            errors.add(new TransacaoInvalidaException(ConstantesMessagem.TID_INVALIDO));
        }
    }
}
