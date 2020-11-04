package fr.abes.hello;

import fr.abes.hello.chunk.LineProcessor;
import fr.abes.hello.chunk.LineReader;
import fr.abes.hello.chunk.LinesWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.annotation.EnableRetry;


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
    protected Step stepProcessLines(ItemReader<String> reader, ItemProcessor<String, String> processor, ItemWriter<String> writer) {
        return stepBuilderFactory
				.get("stepProcessLines").<String, String> chunk(10)
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
    public ItemReader<String> itemReader() {
        return new LineReader();
    }

    @Bean
    public ItemProcessor<String, String> itemProcessor() {
        return new LineProcessor();
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return new LinesWriter();
}
}
