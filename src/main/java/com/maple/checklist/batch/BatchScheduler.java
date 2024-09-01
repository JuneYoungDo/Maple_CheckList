package com.maple.checklist.batch;

import java.time.LocalDateTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class BatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job dailyJob;

    @Autowired
    private Job weeklyJob;

    @Autowired
    private Job monthlyJob;

    @Autowired
    private Job dailyLastJob;

    @Autowired
    private Job deleteCharacterAndListJob;

    @Scheduled(cron = "0 50 23 L * ?")  // 매달 마지막 날 밤 11시 50분에 실행
    public void scheduleMonthlyJob() throws Exception {
        jobLauncher.run(monthlyJob, new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .addLocalDateTime("LocalDateTime", LocalDateTime.now())
            .toJobParameters());
    }

    @Scheduled(cron = "0 50 23 ? * 3")  // 매주 수요일 밤 11시 50분에 실행
    public void scheduleWeeklyJob() throws Exception {
        jobLauncher.run(weeklyJob, new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .addLocalDateTime("LocalDateTime", LocalDateTime.now())
            .toJobParameters());
        jobLauncher.run(deleteCharacterAndListJob, new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .addLocalDateTime("LocalDateTime", LocalDateTime.now())
            .toJobParameters());
    }

    @Scheduled(cron = "0 50 23 * * *")  // 매일 밤 11시 50분에 실행
    public void scheduleDailyJob() throws Exception {
        jobLauncher.run(dailyJob, new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .addLocalDateTime("LocalDateTime", LocalDateTime.now())
            .toJobParameters());
    }

    @Scheduled(cron = "0 57 23 * * *")  // 매일 밤 11시 57분에 실행
    public void scheduleDailyLastJob() throws Exception {
        jobLauncher.run(dailyLastJob, new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .addLocalDateTime("LocalDateTime", LocalDateTime.now())
            .toJobParameters());
    }
}
