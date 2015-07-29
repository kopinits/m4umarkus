package br.com.markus.converter;

import br.com.markus.model.Transacao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface para conversão das entradas JSON e XML para o bean Transacao e vice-versa
 *
 * @author Markus Kopinits
 */
@Component
public interface IConversorTransacao {
    /**
     * Método responsável por converter uma entrada String no formato JSON/XML para o bean Transacao
     *
     * @param transacaoString transação em formato xml ou json para ser convertida
     * @return Transacao transacao convertida
     * @throws Exception
     * @see br.com.markus.converter.impl.ConversorTransacaoBrandX
     * @see br.com.markus.converter.impl.ConversorTransacaoBrandY
     */
    Transacao toTransacao(String transacaoString) throws Exception;

    ArrayList<Transacao> toTransacaoConsulta(String transacaoString) throws Exception;

    /**
     * Método responsável por converter um bean Transacao para uma String no formato JSON/XML
     *
     * @param transacao transação que será convertida
     * @return String no formato JSON/XML
     * @throws Exception
     * @see br.com.markus.converter.impl.ConversorTransacaoBrandX
     * @see br.com.markus.converter.impl.ConversorTransacaoBrandY
     */
    String fromTransacao(Transacao transacao) throws Exception;

    /**
     * Método responsável por converter uma lista de beans Transacao para uma String no formato JSON/XML
     *
     * @param transacaoList lista de transações que serão convertidas
     * @return String no formato JSON/XML
     * @throws Exception
     * @see br.com.markus.converter.impl.ConversorTransacaoBrandX
     * @see br.com.markus.converter.impl.ConversorTransacaoBrandY
     */
    String fromTransacao(List<Transacao> transacaoList) throws Exception;

    /**
     * Método que retorna a mensagem de serviço Offline
     * @return menagem informativa de serviço offline
     */
    String getMensagemServicoOff();

}
