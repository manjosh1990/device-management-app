package com.manjosh.labs.commandservice.repository;

import com.manjosh.labs.commandservice.model.Command;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandRepository extends JpaRepository<Command, Long> {}
