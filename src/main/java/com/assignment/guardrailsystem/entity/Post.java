package com.assignment.guardrailsystem.entity;

import com.assignment.guardrailsystem.enums.AuthorType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authorId;

    @Enumerated(EnumType.STRING)
    private AuthorType authorType;

    @Column(length = 2000)
    private String content;

    private LocalDateTime createdAt;
}