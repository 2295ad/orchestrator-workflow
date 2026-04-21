package com.jswone.orchestrator.http.controller;

import com.jswone.orchestrator.dto.OrchestratorResponse;
import com.jswone.orchestrator.service.OrchestrationService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sync")
public class OrchestratorController {

  @Autowired OrchestrationService orchestrationService;

  @PostMapping(value = "/v1")
  public ResponseEntity<OrchestratorResponse<Boolean>> invokeWorkflow(
      @RequestBody Map<String, Object> payload) {
    Boolean result = orchestrationService.invokeWorkflow(payload);
    OrchestratorResponse<Boolean> response =
        OrchestratorResponse.<Boolean>builder()
            .success(result)
            .message(result ? "Workflow executed successfully" : "Workflow execution failed")
            .data(result)
            .build();
    return ResponseEntity.ok(response);
  }
}
