package fr.abes.helloabes.batch.chunk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
public class LinesWriter implements ItemWriter<String>, StepExecutionListener {

    PrintWriter out;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        try {
            out = new PrintWriter(new FileWriter("C:\\dev\\abes-hello\\res.txt"));
        } catch (IOException e) {
            log.error(e.toString());
        }
        log.info("Line Writer initialized.");
    }

	@Override
    public void write(List<? extends String>lines) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
			stringBuilder.append(line + "...");
            out.println(line);
        }
		log.info("dans le writer : " + stringBuilder.toString());

    }
	
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        out.close();
        return ExitStatus.COMPLETED;
    }
}