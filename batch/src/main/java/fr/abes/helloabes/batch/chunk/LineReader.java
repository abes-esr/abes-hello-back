package fr.abes.helloabes.batch.chunk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LineReader implements ItemReader<String>, StepExecutionListener {

	List<String> lines;

	AtomicInteger i = new AtomicInteger();

	@Override
    public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution
				.getJobExecution()
				.getExecutionContext();
		this.lines = (List<String>) executionContext.get("lines");
    }

    @Override
    public String read() throws Exception {

		String line = null;
		if (i.intValue() < this.lines.size()) {
			line = lines.get(i.getAndIncrement());
		}
		return line;

    }
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Line Reader ended.");
        return ExitStatus.COMPLETED;
    }
}