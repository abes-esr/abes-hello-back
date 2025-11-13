package fr.abes.helloabes.batch;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UneTasklet implements Tasklet, StepExecutionListener  {

	List<AppUser> users = new ArrayList<>();


	private final IUserService service;

	public UneTasklet(IUserService service) {
		this.service = service;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
        log.info("UneTasklet.java/beforeStep. Nom du step en cours : {}", stepExecution.getStepName());
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
		log.info("UneTasklet.java/execute");
		try {
			users = service.getAllUsers();
            log.debug("Taille de la liste utilisateurs : {}", users.size());
		}
		catch (Exception e) {
            log.error("Erreur dans la tasklet : {}", String.valueOf(e));
		}
		return RepeatStatus.FINISHED;
	}


	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("UneTasklet.java/afterStep");
		ExecutionContext executionContext = stepExecution
				.getJobExecution()
				.getExecutionContext();
		executionContext.put("userList", this.users);
		return stepExecution.getExitStatus();
	}
}
