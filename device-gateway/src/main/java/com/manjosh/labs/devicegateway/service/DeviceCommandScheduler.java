package com.manjosh.labs.devicegateway.service;

import com.manjosh.labs.devicecontracts.models.CommandEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceCommandScheduler {

  private final Map<String, ExecutorService> executors = new ConcurrentHashMap<>();

  private final DeviceCommandExecutor executor;

  public void schedule(final CommandEvent command) {

    try (ExecutorService deviceExecutor =
        executors.computeIfAbsent(
            command.getDeviceId(), id -> Executors.newSingleThreadExecutor())) {

      deviceExecutor.submit(() -> executor.execute(command));
    }
  }
}
