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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UneTasklet implements Tasklet, StepExecutionListener  {

	List<AppUser> users = new ArrayList<>();

	@Autowired
	IUserService service;

	@Override
	public void beforeStep(StepExecution stepExecution) {

	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

		log.info("DANS LA TASKLET");

		try
		{
			users = service.getAllUsers();
			log.debug("Users list size ="+Integer.toString(users.size()));
		}
		catch (Exception e)
		{
			log.error("erreur dans la tasklet :" + e);
		}

		return RepeatStatus.FINISHED;
	}


	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution
				.getJobExecution()
				.getExecutionContext();
		executionContext.put("userList", this.users);
		return stepExecution.getExitStatus();
	}
}
