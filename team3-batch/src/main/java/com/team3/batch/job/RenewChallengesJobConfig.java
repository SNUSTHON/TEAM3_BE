package com.team3.batch.job;

import com.team3.batch.listener.StopWatchJobListener;
import com.team3.batch.rowmapper.RandomChallengeRowMapper;
import com.team3.batch.validator.DateParameterValidator;
import com.team3.batch.vo.RandomChallengeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class RenewChallengesJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;
    private final DataSource dataSource;

    private int chunkSize;
    private int poolSize;

    @Value("${chunkSize:1000}")
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Value("${poolSize:10}")
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    @Bean
    public TaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize * 2);
        executor.setThreadNamePrefix("multi-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();
        return executor;
    }

    @Bean
    public Job renewChallengesJob() throws Exception {
        return new JobBuilder("renewChallengesJob", jobRepository)
                .validator(new DateParameterValidator())
                .incrementer(new RunIdIncrementer())
                .start(renewChallengesStep())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step renewChallengesStep() throws Exception {
        return new StepBuilder("renewChallengesStep", jobRepository)
                .<RandomChallengeVO, RandomChallengeVO>chunk(chunkSize, tx)
                .reader(randomChallengesReader(null))
                .writer(renewChallengesWriter())
                .taskExecutor(executor())
                .build();
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<RandomChallengeVO> randomChallengesReader(@Value("#{jobParameters['date']}") String date) throws Exception {
        String sql = "SELECT member_id, challenge_id, category_id " +
                "FROM ( " +
                "SELECT " +
                "m.id AS member_id, " +
                "c.id AS challenge_id, " +
                "cl.category_id, " +
                "ROW_NUMBER() OVER (PARTITION BY m.id, cl.category_id ORDER BY RAND()) AS rn " +
                "FROM member m " +
                "INNER JOIN category_level cl ON m.id = cl.member_id AND cl.is_selected = 1 " +
                "INNER JOIN challenge c ON c.category_id = cl.category_id AND c.level = cl.level " +
                ") sub " +
                "WHERE sub.rn <= 2";

        JdbcCursorItemReader<RandomChallengeVO> itemReader = new JdbcCursorItemReaderBuilder<RandomChallengeVO>()
                .name("randomChallengesReader")
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new RandomChallengeRowMapper())
                .sql(sql)
                .saveState(false)
                .build();

        return new SynchronizedItemStreamReaderBuilder<RandomChallengeVO>()
                .delegate(itemReader)
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<RandomChallengeVO> renewChallengesWriter() {
        return new JdbcBatchItemWriterBuilder<RandomChallengeVO>()
                .dataSource(dataSource)
                .sql("INSERT INTO member_challenge (member_id, challenge_id, created_at, updated_at) VALUES (:memberId, :challengeId, now(), now())")
                .beanMapped()
                .build();
    }

}
