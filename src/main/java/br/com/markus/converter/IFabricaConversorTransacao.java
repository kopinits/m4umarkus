package br.com.markus.converter;

import br.com.markus.enuns.TipoBandeiraCartaoEnum;

/**
 * Interface para a Fábrica de Conversores
 */
public interface IFabricaConversorTransacao {
    /**
     * Método responsável por retornar a implementação do converter para o tipo passado
     *
     * @param tipo tipo da bandeira para recuperar o conversor correto
     * @return Implementação do conversor para o tipo passado
     */
    IConversorTransacao getConverter(TipoBandeiraCartaoEnum tipo);

}
