package com.assignment.guardrailsystem.repository;

import com.assignment.guardrailsystem.entity.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository extends JpaRepository<Bot, Long> {
}
