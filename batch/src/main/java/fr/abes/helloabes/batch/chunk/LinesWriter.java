package fr.abes.helloabes.batch.chunk;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
    public void write(List<? extends String>lines) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
			stringBuilder.append(line + "...");
            out.println(line);
            counTer++;
        }
		log.info("dans le writer : " + stringBuilder.toString());

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        out.println(String.format("===== Il y a %s utilisateurs dans la base =====", counTer));
        out.close();
        return ExitStatus.COMPLETED;
    }
}
