package fr.abes.helloabes.batch.chunk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class LinesWriter implements ItemWriter<String>, StepExecutionListener {

    @Value("${FILE.OUT.PUT}")
    private String source;
    private PrintWriter out;
    private Long counTer;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        counTer = 0L;
        try {
            out = new PrintWriter(new FileWriter(source));
        } catch (IOException e) {
            log.error(e.toString());
        }
        log.info("Line Writer initialized.");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        out.println(String.format("===== Il y a %s utilisateurs dans la base =====", counTer));
        out.close();
        return ExitStatus.COMPLETED;
    }

    @Override
    public void write(Chunk<? extends String> chunk) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : chunk) {
            stringBuilder.append(line + "...");
            out.println(line);
            counTer++;
        }
        log.info("dans le writer : " + stringBuilder);
    }
}
