package com.assignment.guardrailsystem.dto;

import com.assignment.guardrailsystem.enums.AuthorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCommentRequest {

    @NotNull
    private Long authorId;

    @NotNull
    private AuthorType authorType;

    @NotBlank
    private String content;

    private Long parentCommentId;
}
