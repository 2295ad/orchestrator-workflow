package com.jswone.orchestrator.jobs.service;

import java.util.Map;

public interface SyncService {
  Boolean initiateSyncWorkflow(Map<String, Object> payload);
}
