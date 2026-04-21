package com.jswone.orchestrator.service;

import java.util.Map;

public interface OrchestrationService {

  Boolean invokeWorkflow(Map<String, Object> payload);
}
