package kakao_tech_bootcamp.community.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Log4j2
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cores = Runtime.getRuntime().availableProcessors(); // 12
        executor.setCorePoolSize(cores * 2); // (코어수 * 2)로 설정
        executor.setMaxPoolSize(cores * 2 * 3); // 평균 트래픽의 3배 대비
        executor.setQueueCapacity(cores * 2 * 50 / 10); // 쓰레드 풀 크기 * 예상 작업 처리 시간 / 평균 작업 생성 시간
        executor.setThreadNamePrefix("async-post-stat-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                log.error("** 비동기 예외 발생 in {} : {}", method.getName(), ex.getMessage());
    }
}
