package com.jswone.orchestrator.jobs.service;

import java.util.Map;

public interface OrchestrationService {

  Boolean invokeWorkflow(Map<String, Object> payload);
}
