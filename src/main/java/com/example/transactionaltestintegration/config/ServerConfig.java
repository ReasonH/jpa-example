package com.example.transactionaltestintegration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ServerConfig implements AsyncConfigurer {

    @Bean
    @Primary
    public TaskExecutor defaultTaskExecutor() {
        return buildThreadPoolTaskExecutor(10, "transaction");
    }

    private ThreadPoolTaskExecutor buildThreadPoolTaskExecutor(int coreSize, String threadNamePrefix) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(coreSize);
        taskExecutor.setMaxPoolSize(coreSize);
        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        return taskExecutor;
    }
}
