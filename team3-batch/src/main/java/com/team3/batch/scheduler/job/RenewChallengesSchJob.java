package com.team3.batch.scheduler.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RenewChallengesSchJob extends QuartzJobBean {

    @Autowired
    private Job renewChallengesJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        String date = (String) context.getJobDetail().getJobDataMap().get("date");

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        try {
            // job의 인스턴스 갯수를 가져옴
            int jobInstanceCount = (int) jobExplorer.getJobInstanceCount(renewChallengesJob.getName());

            // 해당 job의 모든 인스턴스 정보를 가져옴. (0 ~ 인스턴스 갯수까지)
            List<JobInstance> jobInstances = jobExplorer.getJobInstances(renewChallengesJob.getName(), 0, jobInstanceCount);

            if (!jobInstances.isEmpty()) {
                for (JobInstance jobInstance : jobInstances) {
                    List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
                    List<JobExecution> sameDateJobExecutionList = jobExecutions.stream().filter(
                            jobExecution -> jobExecution.getJobParameters().getString("date").equals(date)
                    ).toList();

                    if (!sameDateJobExecutionList.isEmpty()) {
                        throw new JobExecutionException(date + "에 대한 스케줄 작업은 이미 수행되었습니다.");
                    }
                }
            }
        } catch (NoSuchJobException e) {
            e.printStackTrace();
        }

        try {
            jobLauncher.run(renewChallengesJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
