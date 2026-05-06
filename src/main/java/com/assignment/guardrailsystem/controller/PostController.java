package com.assignment.guardrailsystem.controller;

import com.assignment.guardrailsystem.dto.CreateCommentRequest;
import com.assignment.guardrailsystem.dto.CreatePostRequest;
import com.assignment.guardrailsystem.entity.Comment;
import com.assignment.guardrailsystem.entity.Post;
import com.assignment.guardrailsystem.service.PostService;
import com.assignment.guardrailsystem.service.ViralityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ViralityService viralityService;

    @PostMapping
    public ResponseEntity<Post> createPost(
            @Valid @RequestBody CreatePostRequest request
    ) {

        return ResponseEntity.ok(
                postService.createPost(request)
        );
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request
    ) {

        return ResponseEntity.ok(
                postService.addComment(postId, request)
        );
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(
            @PathVariable Long postId
    ) {

        postService.likePost(postId);

        return ResponseEntity.ok("Post liked successfully");
    }
    @GetMapping("/{postId}/virality")
    public ResponseEntity<Long> getVirality(
            @PathVariable Long postId
    ) {

        return ResponseEntity.ok(
                viralityService.getViralityScore(postId)
        );
    }
}