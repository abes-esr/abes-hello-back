package fr.abes.helloabes.batch;

import fr.abes.helloabes.batch.chunk.LineProcessor;
import fr.abes.helloabes.batch.chunk.LineReader;
import fr.abes.helloabes.batch.chunk.LinesWriter;
import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.entities.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Configuration
@EnableRetry
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    protected JobBuilderFactory jobs;

    @Autowired
    protected StepBuilderFactory stepBuilderFactory;

    // ---------- JOB ---------------------------------------------

    @Bean
    public Job jobTraitement() {
        log.info("debut du job : jobTraitement...");  

        return jobs
                .get("chunksJob")
                .start(executerTasklet())
                .next(stepProcessLines(itemReader(), itemProcessor(), itemWriter()))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    // ***************** TASK EXECUTOR **************************
    @Bean
    public TaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch");
    }



    // ---------- STEP --------------------------------------------

    @Bean
    public Step executerTasklet() {
        return stepBuilderFactory
                .get("executerTasklet").allowStartIfComplete(true)
                .tasklet(uneTasklet())
                .build();
    }

    @Bean
    protected Step stepProcessLines(ItemReader<AppUser> reader, ItemProcessor<AppUser, String> processor, ItemWriter<String> writer) {
        return stepBuilderFactory
                .get("stepProcessLines").<AppUser, String> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                //.taskExecutor(taskExecutor())
                //.throttleLimit(10)
                .build();
    }

    // ------------- TASKLETS -----------------------
    @Bean
    public UneTasklet uneTasklet()
    {
        return new UneTasklet();
    }


    // ----------------- CHUNKS ------------------------------

    @Bean
    public ItemReader<AppUser> itemReader() {
        return new LineReader();
    }

    @Bean
    public ItemProcessor<AppUser, String> itemProcessor() {
        return new LineProcessor();
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return new LinesWriter();
    }
}