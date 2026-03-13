package com.manjosh.labs.commandservice.controller;

import com.manjosh.labs.commandservice.dto.CommandRequest;
import com.manjosh.labs.commandservice.service.CommandService;
import com.manjosh.labs.devicecontracts.models.CommandResponse;
import com.manjosh.labs.devicecontracts.models.CommandStatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands")
@RequiredArgsConstructor
public class CommandController {

  private final CommandService commandService;

  @PostMapping
  public CommandResponse createCommand(@RequestBody final CommandRequest request) {
    return commandService.createCommand(request);
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<Void> updateStatus(
      @PathVariable Long id, @RequestBody final CommandStatusUpdateRequest request) {
    commandService.updateStatus(id, request.getStatus());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public CommandResponse getCommand(@PathVariable final Long id) {
    return commandService.getCommand(id);
  }
}
