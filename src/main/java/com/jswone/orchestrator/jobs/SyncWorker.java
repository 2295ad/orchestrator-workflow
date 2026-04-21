package com.jswone.orchestrator.jobs;

import com.jswone.orchestrator.jobs.activity.ConsumerActivity;
import com.jswone.orchestrator.jobs.workflow.impl.OrchestratorSyncWorkflowImpl;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SyncWorker {

  @Value("${temporal.task-queue}")
  private String taskQueue;

  private final ConsumerActivity consumerActivity;
  private final WorkerFactory workerFactory;

  @PostConstruct
  public void registerWorker() {
    Worker worker = workerFactory.newWorker(taskQueue);
    worker.registerWorkflowImplementationTypes(OrchestratorSyncWorkflowImpl.class);
    worker.registerActivitiesImplementations(consumerActivity);
    try {
      workerFactory.start();
      log.info("worker started on queue: {}", taskQueue);
    } catch (Exception e) {
      log.error("Failed to start worker", e);
      throw new RuntimeException("Unable to initiate worker", e);
    }
  }
}
