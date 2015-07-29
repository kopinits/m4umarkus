package br.com.markus.enuns;

/**
 * Enum que contém os tipos de pagamentos
 */
public enum TipoPagamentoEnum {
    AVISTA(0L, "AVISTA"),
    PARCELADO_LOJA(1L, "PARCELADO LOJA"),
    PARCELADO_ADM(2L, "PARCELADO ADM");

    private Long id;
    private String descricao;

    TipoPagamentoEnum(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public static TipoPagamentoEnum from(final String valor) {
        if (valor == null) {
            return null;
        }

        for (TipoPagamentoEnum e : TipoPagamentoEnum.values()) {
            if (valor.equals(e.getDescricao())) {
                return e;
            }
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
