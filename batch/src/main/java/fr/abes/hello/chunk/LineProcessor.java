package fr.abes.hello.chunk;

import fr.abes.hello.ProxyRetry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class LineProcessor implements ItemProcessor<String, String>, StepExecutionListener {

    @Autowired
    ProxyRetry proxyRetry;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Line Processor initialized.");
    }

    @Override
    public String process(String line) throws Exception {

        try {
            line = this.proxyRetry.getLine(line);
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