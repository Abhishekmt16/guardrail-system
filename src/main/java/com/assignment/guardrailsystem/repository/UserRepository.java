package com.assignment.guardrailsystem.repository;

import com.assignment.guardrailsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
