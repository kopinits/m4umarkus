package br.com.markus.webservice.util;

import br.com.markus.exception.MultipleTransacaoInvalidaException;
import br.com.markus.exception.TransacaoInvalidaException;
import br.com.markus.message.ConstantesMessagem;

/**
 * Classe para varrer a exceção e retornar os erros
 */
public class TransacaoExceptionHandler {

    public static String retornaExcecao(Exception e){
        StringBuilder excecao = new StringBuilder();
        if (e instanceof MultipleTransacaoInvalidaException) {
            MultipleTransacaoInvalidaException me = (MultipleTransacaoInvalidaException) e;
            for (TransacaoInvalidaException exception : me.getExceptions()) {
                excecao.append(exception.getMessage());
            }
        }else if (e instanceof  TransacaoInvalidaException){
            TransacaoInvalidaException exception = (TransacaoInvalidaException) e;
            excecao.append(exception.getMessage());
        }else{
            excecao.append(ConstantesMessagem.ERRO_GENERICO);
        }
        return excecao.toString();
    }

}
