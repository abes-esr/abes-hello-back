package fr.abes.helloabes.batch;

import fr.abes.helloabes.batch.chunk.LineProcessor;
import fr.abes.helloabes.batch.chunk.LineReader;
import fr.abes.helloabes.batch.chunk.LinesWriter;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableRetry
public class BatchConfiguration {

    private final JobRepository jobRepository;

    private final IUserService service;

    public BatchConfiguration(JobRepository jobRepository, IUserService service) {
        this.jobRepository = jobRepository;
        this.service = service;
    }

    // ----------------- CHUNKS ------------------------------

    @Bean
    public ItemReader<AppUser> reader() {
        return new LineReader();
    }

    @Bean
    public ItemProcessor<AppUser, String> processor() {
        return new LineProcessor(new ProxyRetry());
    }

    @Bean
    public ItemWriter<String> writer() {
        return new LinesWriter();
    }

    // ------------- TASKLETS -----------------------
    @Bean
    public UneTasklet uneTasklet() {
        return new UneTasklet(service);
    }

    // ---------- STEP --------------------------------------------

    @Bean
    public Step runUneTasklet(JobRepository jobRepository, @Qualifier("uneTasklet") Tasklet tasklet, PlatformTransactionManager transactionManager) {
        log.info("Dans runUneTasklet");
        return new StepBuilder("runUneTasklet", jobRepository).allowStartIfComplete(true)
                .tasklet(uneTasklet(), transactionManager)
                .build();
    }

    @Bean
    protected Step stepProcessLines(PlatformTransactionManager transactionManager, ItemReader<AppUser> reader, ItemProcessor<AppUser, String> processor, ItemWriter<String> writer) {
        log.info("Dans stepProcessLines");
        return new StepBuilder("stepProcessLines", jobRepository)
                .<AppUser, String> chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // ---------- JOB ---------------------------------------------

    @Bean
    public Job jobTraitement(JobRepository jobRepository,
                             @Qualifier("runUneTasklet") Step step1,
                             @Qualifier("stepProcessLines") Step step2) {
        log.info("debut du job : jobTraitement...");  

        return new JobBuilder("jobTraitement", jobRepository).incrementer(new RunIdIncrementer())
                .start(step1).on("FAILED").end()
                .from(step1).on("COMPLETED").to(step2)

                .from(step2).on("FAILED").end()
                .from(step2).on("COMPLETED").stop()
                .build().build();
    }

    // ***************** TASK EXECUTOR **************************
    @Bean
    public TaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

}