package com.maple.checklist.batch;

import com.maple.checklist.domain.list.usecase.UpdateListUseCase;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final UpdateListUseCase updateListUseCase;

    public Job createJob(String jobType) {
        return new JobBuilder(jobType + "_JOB", jobRepository)
            .start(cleanupStep(jobType))
            .listener(new JobExecutionListener() {
                @Override
                public void beforeJob(JobExecution jobExecution) {
                    log.info("===========================JOB START==========================");
                    log.info("===== Job Name : {}", jobExecution.getJobInstance().getJobName());
                    log.info("==================== Job Parameters ==========================");
                    jobExecution.getJobParameters().getParameters().forEach((key, value) ->
                        log.info("===== {} : {}", key, value.getValue())
                    );
                    log.info("==============================================================");
                }

                @Override
                public void afterJob(JobExecution jobExecution) {
                    log.info("==============================================================");
                    log.info("===== Job Name            : {}",
                        jobExecution.getJobInstance().getJobName());
                    log.info("===== Job Status          : {}", jobExecution.getStatus());
                    if (jobExecution.getStartTime() != null && jobExecution.getEndTime() != null) {
                        Duration duration = Duration.between(jobExecution.getStartTime(),
                            jobExecution.getEndTime());
                        long seconds = duration.getSeconds();
                        log.info("===== Job Processing Time : {} seconds", seconds);
                    }
                    log.info("============================JOB END===========================");
                }
            })
            .build();
    }

    public Step cleanupStep(String jobType) {
        return new StepBuilder("CleanUp_" + jobType, jobRepository)
            .tasklet(cleanupTasklet(jobType), platformTransactionManager)
            .listener(new StepExecutionListenerSupport() {
                @Override
                public void beforeStep(StepExecution stepExecution) {
                }

                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    printStepLog(stepExecution);
                    return ExitStatus.COMPLETED;
                }
            })
            .build();
    }

    public Tasklet cleanupTasklet(String jobType) {
        return (contribution, chunkContext) -> {
            switch (jobType) {
                case "DAILY" -> updateListUseCase.resetDaily();
                case "WEEKLY" -> updateListUseCase.resetWeekly();
                case "MONTHLY" -> updateListUseCase.resetMonthly();
                default -> throw new IllegalArgumentException("Unknown job type: " + jobType);
            }
            return RepeatStatus.FINISHED;
        };
    }

    private void printStepLog(StepExecution stepExecution) {
        log.info("==============================================================");
        log.info("===== Step Name       : {}", stepExecution.getStepName());
        log.info("===== Start Time      : {}", stepExecution.getStartTime());
        log.info("===== End Time        : {}", LocalDateTime.now());
        log.info("===== ExitStatus      : {}", stepExecution.getExitStatus().getExitCode());
        log.info("===== ReadCount       : {}", stepExecution.getReadCount());
        log.info("===== ReadSkipCount   : {}", stepExecution.getReadSkipCount());
        log.info("===== ProcessCount    : {}", stepExecution.getProcessSkipCount());
        log.info("===== WriteCount      : {}", stepExecution.getWriteCount());
        log.info("===== WriteSkipCount  : {}", stepExecution.getWriteSkipCount());
        log.info("===== FilterCount     : {}", stepExecution.getFilterCount());
        log.info("===== CommitCount     : {}", stepExecution.getCommitCount());
        log.info("===== RollbackCount   : {}", stepExecution.getRollbackCount());
        log.info("===== SkipCount       : {}", stepExecution.getSkipCount());
        log.info("=================== ExitDescription ========================");
        log.info("{}", stepExecution.getExitStatus().getExitDescription());
        log.info("==============================================================");
    }
}
