package com.manjosh.labs.devicegateway.config;

import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class ExecutorConfig {

  private final CommandExecutorProperties commandExecutorProperties;

  @Bean(name = "deviceCommandExecutorPool")
  public Executor deviceCommandExecutorPool() {
    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(commandExecutorProperties.getCorePoolSize());
    executor.setMaxPoolSize(commandExecutorProperties.getMaxPoolSize());
    executor.setQueueCapacity(commandExecutorProperties.getQueueCapacity());
    executor.setThreadNamePrefix("device-command-worker-");
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(commandExecutorProperties.getAwaitTerminationSeconds());
    executor.initialize();
    return executor;
  }
}
