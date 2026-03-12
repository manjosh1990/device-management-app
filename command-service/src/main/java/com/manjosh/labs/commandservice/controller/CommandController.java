package com.manjosh.labs.commandservice.controller;

import com.manjosh.labs.commandservice.dto.CommandRequest;
import com.manjosh.labs.commandservice.model.Command;
import com.manjosh.labs.commandservice.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commands")
@RequiredArgsConstructor
public class CommandController {

  private final CommandService commandService;

  @PostMapping
  public Command createCommand(@RequestBody final CommandRequest request) {

    return commandService.createCommand(request);
  }
}
