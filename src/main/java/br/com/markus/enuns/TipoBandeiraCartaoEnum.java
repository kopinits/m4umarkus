package br.com.markus.enuns;

/**
 * Enum que contém os tipos de conversores
 */
public enum TipoBandeiraCartaoEnum {
    XML("XML", "BrandX"),
    JSON("JSON", "BrandY");

    private String descricao;
    private String bandeira;

    TipoBandeiraCartaoEnum(String descricao, String bandeira) {
        this.descricao = descricao;
        this.bandeira = bandeira;
    }

    public static TipoBandeiraCartaoEnum from(final String valor) {
        if (valor == null) {
            return null;
        }

        for (TipoBandeiraCartaoEnum e : TipoBandeiraCartaoEnum.values()) {
            if (valor.equals(e.getDescricao()) || valor.equals(e.getBandeira())) {
                return e;
            }
        }
        return null;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
