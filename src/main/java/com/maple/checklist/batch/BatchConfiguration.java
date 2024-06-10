package com.maple.checklist.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final BatchService batchService;

    @Bean
    public Job dailyJob() {
        return batchService.createJob("DAILY");
    }

    @Bean
    public Job weeklyJob() {
        return batchService.createJob("WEEKLY");
    }

    @Bean
    public Job monthlyJob() {
        return batchService.createJob("MONTHLY");
    }
}

