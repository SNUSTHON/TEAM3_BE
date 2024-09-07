package com.team3.batch;

import com.team3.batch.job.RenewChallengesJobConfig;
import com.team3.core.domain.category.dao.CategoryRepository;
import com.team3.core.domain.challenge.dao.ChallengeRepository;
import com.team3.core.domain.challenge.dao.MemberChallengeRepository;
import com.team3.core.domain.challenge.domain.MemberChallenge;
import com.team3.core.global.auth.model.OAuth2Provider;
import com.team3.core.global.auth.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {RenewChallengesJobConfig.class, TestBatchConfig.class})
@TestPropertySource(properties = {"chunkSize=1", "poolSize=1"})
@Sql("/data.sql")
public class RenewChallengesJobTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    MemberChallengeRepository memberChallengeRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

//    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize = 10;


    @Test
    @DisplayName("renewChallengesJobTest")
    void renewChallengesJobTest() throws Exception {
        // given
        int totalSize = 10;
        log.info("batch size : {}", batchSize);
        log.info("members size : {}", totalSize);

        for (int batchCount = 0; batchCount < totalSize / batchSize; batchCount++) {
            batchInsertMember(batchCount);
            log.info("batchCount : {}", batchCount);
        }

        log.info("배치 INSERT 작업 완료");

        for (long memberId = 1; memberId <= totalSize; memberId++) {
            insertCategoryLevel(memberId);
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", "2024-01-08")
                .toJobParameters();
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        // then
        List<MemberChallenge> memberChallenges = memberChallengeRepository.findAll();
        LocalDate today = LocalDate.now();
        LocalDateTime startAt = today.atStartOfDay();
        LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(23, 59, 59, 999999999));
        List<MemberChallenge> firstMembersChallenges = memberChallengeRepository.findMemberChallengesByMemberIdAndDate(1L, startAt, endAt);
        List<MemberChallenge> secondMembersChallenges = memberChallengeRepository.findMemberChallengesByMemberIdAndDate(2L, startAt, endAt);

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(memberChallenges).hasSize(6 * totalSize);
        assertThat(firstMembersChallenges).hasSize(6);
        assertThat(secondMembersChallenges).hasSize(6);

    }

    private void batchInsertMember(int batchCount) {
        String sql = "INSERT INTO member" +
                " (username, email, provider, provider_id, role, profile_image_url, created_at, updated_at)" +
                " VALUES (?, ?, ?, ?, ?, ?, now(), now())";
        int finalBatchCount = batchCount;
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                int index = 1000 * finalBatchCount + i;
                ps.setString(1, "tester" + index);
                ps.setString(2, "test" + index + "@test.com");
                ps.setString(3, OAuth2Provider.GOOGLE.name());
                ps.setString(4, "google_foobarfoobar" + index);
                ps.setString(5, Role.ROLE_USER.name());
                ps.setString(6, "img");
            }

            @Override
            public int getBatchSize() {
                return batchSize;
            }
        });
    }

    private void insertCategoryLevel(Long memberId) {
        String sql = "INSERT INTO category_level" +
                " (member_id, category_id, is_selected, created_at, updated_at)" +
                " VALUES (?, ?, ?, now(), now())";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, memberId);
                ps.setLong(2, i + 1);
                ps.setBoolean(3, i < 3);
            }

            @Override
            public int getBatchSize() {
                return 8;
            }
        });
    }
}
