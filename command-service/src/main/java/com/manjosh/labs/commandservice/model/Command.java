package com.manjosh.labs.commandservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Command {

  @Id @GeneratedValue private Long id;

  private String deviceId;

  @Enumerated(EnumType.STRING)
  private CommandType commandType;

  @Enumerated(EnumType.STRING)
  private CommandStatus status;

  private String payload;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
