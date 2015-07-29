package br.com.markus.model;

import br.com.markus.enuns.TipoBandeiraCartaoEnum;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe responsável por conter os atributos da solicitação de pagamento
 *
 * @author Markus Kopinits
 */

@XmlRootElement(name = "transacao")
public final class Transacao {

    private String tid;
    private DadosPedido dadosPedido;
    private FormaPagamento formaPagamento;
    private TipoBandeiraCartaoEnum tipoBandeiraCartaoEnum;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    @XmlElement(name = "dados-pedido")
    public DadosPedido getDadosPedido() {
        return dadosPedido;
    }

    public void setDadosPedido(DadosPedido dadosPedido) {
        this.dadosPedido = dadosPedido;
    }

    @XmlElement(name = "forma-pagamento")
    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public TipoBandeiraCartaoEnum getTipoBandeiraCartaoEnum() {
        return tipoBandeiraCartaoEnum;
    }

    public void setTipoBandeiraCartaoEnum(TipoBandeiraCartaoEnum tipoBandeiraCartaoEnum) {
        this.tipoBandeiraCartaoEnum = tipoBandeiraCartaoEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Transacao transacao = (Transacao) o;

        return new EqualsBuilder()
                .append(tid, transacao.tid).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tid).toHashCode();
    }

    @Override
    public String toString() {
        return "{transacao:{" +
                "tid:'" + tid + '\'' +
                ", dados-pedido:{" + dadosPedido +
                ", forma-pagamento:{" + formaPagamento +
                "}}";
    }
}
