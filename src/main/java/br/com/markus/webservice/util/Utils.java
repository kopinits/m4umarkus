package br.com.markus.webservice.util;

import br.com.markus.enuns.TipoBandeiraCartaoEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * Métodos úteis
 */
public final class Utils {
    public static TipoBandeiraCartaoEnum descobrirTipoRequisicao(String requisicaoString) {
        if (StringUtils.isNotBlank(requisicaoString)) {
            if (requisicaoString.trim().startsWith("<")) {
                return TipoBandeiraCartaoEnum.XML;
            } else {
                return TipoBandeiraCartaoEnum.JSON;
            }
        }
        return null;
    }
}
