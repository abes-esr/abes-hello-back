package fr.abes.helloabes.batch.chunk;

import fr.abes.helloabes.batch.ProxyRetry;
import fr.abes.helloabes.core.entities.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class LineProcessor implements ItemProcessor<AppUser, String>, StepExecutionListener {

    @Autowired
    ProxyRetry proxyRetry;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Line Processor initialized.");
    }

    @Override
    public String process(AppUser user) throws Exception {
        String line = null;
        try {

            line = this.proxyRetry.getLine(user);
        }
        catch (Exception e) {
            log.error(e.toString());
        }
        log.info(line);
        return line;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Line Processor ended.");
        return ExitStatus.COMPLETED;
    }
}