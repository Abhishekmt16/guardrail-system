package com.assignment.guardrailsystem.repository;

import com.assignment.guardrailsystem.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}