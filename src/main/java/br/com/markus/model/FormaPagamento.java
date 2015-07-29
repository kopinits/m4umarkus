package br.com.markus.model;

import javax.xml.bind.annotation.XmlElement;

/**
 * Classe responsável por conter os atributos da forma de pagamento
 *
 * @author Markus Kopinits
 */
public final class FormaPagamento {
    private String tipo;
    private Integer qtdParcelas;

    @XmlElement(name = "modalidade")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @XmlElement(name = "parcelas")
    public Integer getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(Integer qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }


    @Override
    public String toString() {
        return "tipo:'" + tipo + '\'' +
                ", quantidade:'" + qtdParcelas + '\'' +
                '}';
    }
}
