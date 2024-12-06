package com.gathering.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        // ThreadPoolTaskExecutor로 스레드 풀을 설정
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor(); // 비동기로 실행될 스레드
        executor.setCorePoolSize(2); // 기본적으로 사용할 스레드 수
        executor.setMaxPoolSize(5); // 최대 스레드 수
        executor.setQueueCapacity(100); // 대기 큐 크기
        executor.setThreadNamePrefix("Async-"); // 스레드 이름 접두사 설정
        executor.initialize(); // 초기화
        return executor;
    }
}