package fr.abes.helloabes.batch.chunk;

import fr.abes.helloabes.core.entities.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LineReader implements ItemReader<AppUser>, StepExecutionListener {

    List<AppUser> users;
    AtomicInteger i = new AtomicInteger();
    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();
        this.users = (List<AppUser>) executionContext.get("userList");
    }

    @Override
    public AppUser read() throws Exception {
        AppUser user = null;

        if (i.intValue() < this.users.size()) {
            user = users.get(i.getAndIncrement());
        }
        return user;
    }
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Line Reader ended.");
        return ExitStatus.COMPLETED;
    }
}