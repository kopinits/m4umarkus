package br.com.markus.scheduler;

import br.com.markus.business.ITransacaoBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Classe responsável por agendar a execução de tarefas para processar os pagamentos assíncronos
 */
@Component
public class JobProcessarTransacao {

    @Autowired
    private ITransacaoBusiness transacaoBusiness;

    @Scheduled(fixedDelay = 120000)
    public void processarTransacoesAssincronas() {
        System.out.println("JOB para processar transações assíncronas iniciado");
        transacaoBusiness.processarTransacoesAssincronas();
        System.out.println("JOB para processar transações assíncronas finalizado");
    }
}
