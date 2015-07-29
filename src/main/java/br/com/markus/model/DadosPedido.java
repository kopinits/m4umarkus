package br.com.markus.model;

import br.com.markus.enuns.StatusSolicitacaoPagamentoEnum;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe responsável por conter os atributos dos dados do pedido
 *
 * @author Markus Kopinits
 */
public final class DadosPedido {
    private String numeroCartao;
    private BigDecimal valor;
    private Date data;
    private String descricao;
    private String nsu;
    private String codigoAutorizacao;
    private StatusSolicitacaoPagamentoEnum statusPagamento;
    private StatusSolicitacaoPagamentoEnum statusCancelamento;

    @XmlElement(name = "numero_cartao")
    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @XmlElement(name = "data-hora")
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNsu() {
        return nsu;
    }

    public void setNsu(String nsu) {
        this.nsu = nsu;
    }

    public String getCodigoAutorizacao() {
        return codigoAutorizacao;
    }

    public void setCodigoAutorizacao(String codigoAutorizacao) {
        this.codigoAutorizacao = codigoAutorizacao;
    }

    @XmlElement(name = "statusPagamento")
    public StatusSolicitacaoPagamentoEnum getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusSolicitacaoPagamentoEnum statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public StatusSolicitacaoPagamentoEnum getStatusCancelamento() {
        return statusCancelamento;
    }

    public void setStatusCancelamento(StatusSolicitacaoPagamentoEnum statusCancelamento) {
        this.statusCancelamento = statusCancelamento;
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder("numero_cartao:'" + numeroCartao + '\'');
        toString.append(", valor:'" + valor.toPlainString() + '\'');
        toString.append(", data-hora:'" + new DateTime(data) + '\'');
        toString.append(", descricao:'" + descricao + '\'');
        toString.append(nsu != null ? ", nsu:'" + nsu + '\'' : "");
        toString.append(codigoAutorizacao != null ? ", codigoAutorizacao:'" + codigoAutorizacao + '\'' : "");
        toString.append(statusPagamento != null ? ", status:'" + statusPagamento + '\'' : "");
        toString.append(statusCancelamento != null ? ", statusCancelamento:'" + statusCancelamento + '\'' : "");
        toString.append('}');
        return toString.toString();
    }
}

