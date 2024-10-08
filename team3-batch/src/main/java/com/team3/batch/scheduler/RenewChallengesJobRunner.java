package com.team3.batch.scheduler;

import com.team3.batch.scheduler.job.RenewChallengesSchJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RenewChallengesJobRunner extends JobRunner {

    @Autowired
    private Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {

        String[] sourceArgs = args.getSourceArgs();

        // 스케줄러에서 Job에 대한 상세 정보를 담고 있는 클래스
        JobDetail jobDetail = buildJobDetail(RenewChallengesSchJob.class, "renewChallengesJob", "batch", new HashMap());

        // 스케줄러 시간 정보
        Trigger trigger = buildJobTrigger("0 0 6 * * ?"); // 매일 06시에 실행

        if (sourceArgs.length <= 0) throw new RuntimeException("date 인자를 전달해주세요.");
        jobDetail.getJobDataMap().put("date", sourceArgs[0]);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
