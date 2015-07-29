package br.com.markus.converter.impl;

import br.com.markus.message.ConstantesMessagem;
import br.com.markus.converter.IConversorTransacao;
import br.com.markus.enuns.TipoBandeiraCartaoEnum;
import br.com.markus.model.Transacao;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por converter a solicitação em formato XML para o bean Transacao
 *
 * @author Markus Kopinits
 */
public class ConversorTransacaoBrandX implements IConversorTransacao {

    /**
     * Método responsável por converter a String, no formato XML, da transação para o bean Transacao
     *
     * @param xmlString xml de entrada
     * @return Transacao
     */
    @Override
    public Transacao toTransacao(String xmlString) throws Exception {
        if (StringUtils.isNotBlank(xmlString)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(Transacao.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader stringReader = new StringReader(xmlString);
            Transacao transacao = (Transacao) jaxbUnmarshaller.unmarshal(stringReader);
            //divide-se por 100 para o valor 1000 ser convertido em 10,00
            transacao.getDadosPedido().setValor(transacao.getDadosPedido().getValor().divide(BigDecimal.valueOf(100d)).setScale(2, BigDecimal.ROUND_HALF_UP));
            transacao.setTipoBandeiraCartaoEnum(TipoBandeiraCartaoEnum.XML);
            return transacao;
        } else {
            return null;
        }
    }

    public ArrayList<Transacao> toTransacaoConsulta(String xmlString) throws Exception {
        if (StringUtils.isNotBlank(xmlString)) {
            XStream stream = new XStream(new DomDriver());
            stream.alias("transacao", Transacao.class);
            ArrayList<Transacao> transacaoArrayList = (ArrayList<Transacao>) stream.fromXML(xmlString);
            for (Transacao transacao : transacaoArrayList){
                transacao.setTipoBandeiraCartaoEnum(TipoBandeiraCartaoEnum.XML);
            }
            return transacaoArrayList;
        } else {
            return null;
        }
    }

    /**
     * Método responsável por converter o bean Transacao para a String no formato XML
     *
     * @param transacao transação que será convertida
     * @return String
     */
    @Override
    public String fromTransacao(Transacao transacao) throws Exception {
        if (transacao != null) {
            JAXBContext jaxbContext = JAXBContext.newInstance(Transacao.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();
            //multiplica-se por 100 para o valor 10,00 ser convertido em 1000
            BigDecimal valorPedido = transacao.getDadosPedido().getValor().setScale(2, BigDecimal.ROUND_HALF_UP);
            transacao.getDadosPedido().setValor(valorPedido.multiply(BigDecimal.valueOf(100)).setScale(0));
            jaxbMarshaller.marshal(transacao, writer);
            //voltando o valor original
            transacao.getDadosPedido().setValor(valorPedido);
            return writer.toString();
        } else {
            return "";
        }
    }

    /**
     * Método responsável por converter uma lista de beans Transacao para a String no formato XML
     *
     * @param transacaoList lista de transações que serão convertidas
     * @return String
     */
    @Override
    public String fromTransacao(List<Transacao> transacaoList) throws Exception {
        StringBuilder retorno = new StringBuilder();
        for (Transacao transacao : transacaoList) {
            retorno.append(fromTransacao(transacao));
        }
        retorno.append("</list>");
        return retorno.toString().replace("?>", "?><list>");
    }

    @Override
    public String getMensagemServicoOff() {
        return ConstantesMessagem.MSG_SERVICO_OFFLINE_XML;
    }
}
