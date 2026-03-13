package com.manjosh.labs.devicegateway.service;

import com.manjosh.labs.devicecontracts.models.CommandEvent;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceCommandScheduler {

  private final Map<String, Queue<CommandEvent>> deviceQueues = new ConcurrentHashMap<>();
  private final Set<String> activeDevices = ConcurrentHashMap.newKeySet();

  private final Executor workerPool;
  private final DeviceCommandExecutor commandExecutor;

  public DeviceCommandScheduler(
      @Qualifier("deviceCommandExecutorPool") final Executor workerPool,
      final DeviceCommandExecutor commandExecutor) {
    this.workerPool = workerPool;
    this.commandExecutor = commandExecutor;
  }

  public void schedule(final CommandEvent command) {
    final String deviceId = command.getDeviceId();

    final Queue<CommandEvent> queue =
        deviceQueues.computeIfAbsent(deviceId, id -> new ConcurrentLinkedQueue<>());

    queue.offer(command);

    log.info(
        "Queued command {} for device {}. queueSize={}",
        command.getCommandId(),
        deviceId,
        queue.size());

    dispatch(deviceId);
  }

  private void dispatch(final String deviceId) {
    if (!activeDevices.add(deviceId)) {
      log.debug("Device {} is already being processed. Skipping worker dispatch.", deviceId);
      return;
    }

    log.info("Dispatching worker for device {}", deviceId);
    workerPool.execute(() -> processQueue(deviceId));
  }

  private void processQueue(final String deviceId) {
    final Queue<CommandEvent> queue = deviceQueues.get(deviceId);

    if (queue == null) {
      log.warn("No queue found for device {} during processing. Skipping.", deviceId);
      activeDevices.remove(deviceId);
      return;
    }

    log.info("Started processing queue for device {}", deviceId);

    try {
      CommandEvent command;

      while ((command = queue.poll()) != null) {
        log.info(
            "Processing command {} for device {}. remainingQueueSize={}",
            command.getCommandId(),
            deviceId,
            queue.size());

        commandExecutor.execute(command);
      }

      log.info("Finished processing queue for device {}", deviceId);
    } catch (final Exception ex) {
      log.error("Unexpected scheduler error while processing device {}", deviceId, ex);
    } finally {
      activeDevices.remove(deviceId);

      if (queue.isEmpty()) {
        deviceQueues.remove(deviceId, queue);
        log.debug("Removed empty queue for device {}", deviceId);
      } else {
        log.info(
            "New commands arrived while finishing processing for device {}. Redispatching worker.",
            deviceId);
        dispatch(deviceId);
      }
    }
  }
}
