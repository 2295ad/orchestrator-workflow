package com.jswone.orchestrator.jobs.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import java.util.Map;

@ActivityInterface
public interface ConsumerActivity {

  @ActivityMethod
  Map<String, Object> transactionStatusUpdate(String transaction);

  @ActivityMethod
  Map<String, Object> verifyUser(Map<String, Object> payload);
}
