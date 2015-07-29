package br.com.markus.enuns;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Enum que contém os tipos de pagamentos
 */
@XmlType
@XmlEnum(String.class)
public enum StatusSolicitacaoPagamentoEnum {
    @XmlEnumValue("AUTORIZADO")AUTORIZADO("AUTORIZADO"),
    @XmlEnumValue("NEGADO")NEGADO_BRAND_X("NEGADO"),
    @XmlEnumValue("NAO AUTORIZADO")NEGADO_BRAND_Y("NAO AUTORIZADO"),
    @XmlEnumValue("SALDO INSUFICIENTE")SALDO_INSUFICIENTE_BRAND_X("SALDO INSUFICIENTE"),
    @XmlEnumValue("NEGADA SALDO")SALDO_INSUFICIENTE_BRAND_Y("NEGADA SALDO");

    private String descricao;

    StatusSolicitacaoPagamentoEnum(String descricao) {
        this.descricao = descricao;
    }

    public static StatusSolicitacaoPagamentoEnum from(final String valor) {
        if (valor == null) {
            return null;
        }

        for (StatusSolicitacaoPagamentoEnum e : StatusSolicitacaoPagamentoEnum.values()) {
            if (valor.equals(e.getDescricao())) {
                return e;
            }
        }
        return null;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
