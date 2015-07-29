package br.com.markus.converter.impl;

import br.com.markus.converter.IConversorTransacao;
import br.com.markus.converter.IFabricaConversorTransacao;
import br.com.markus.enuns.TipoBandeiraCartaoEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por criar o conversor de XMl/JSON da transação para o bean Transacao
 *
 * @author Markus Kopinits
 */
@Component
public class FabricaConversorTransacao implements IFabricaConversorTransacao {

    private Map<TipoBandeiraCartaoEnum, IConversorTransacao> conversores;

    public FabricaConversorTransacao() {
        conversores = new HashMap<TipoBandeiraCartaoEnum, IConversorTransacao>();
        conversores.put(TipoBandeiraCartaoEnum.XML, new ConversorTransacaoBrandX());
        conversores.put(TipoBandeiraCartaoEnum.JSON, new ConversorTransacaoBrandY());
    }

    /**
     * Método responsável por retornar a implementação do converter para o tipo passado
     *
     * @param tipo tipo da bandeira para recuperar o conversor correto
     * @return Implementação do conversor para o tipo passado
     */
    public IConversorTransacao getConverter(TipoBandeiraCartaoEnum tipo) {
        return conversores.get(tipo);
    }
}

