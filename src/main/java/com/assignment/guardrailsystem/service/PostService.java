package com.assignment.guardrailsystem.service;

import com.assignment.guardrailsystem.dto.CreateCommentRequest;
import com.assignment.guardrailsystem.dto.CreatePostRequest;
import com.assignment.guardrailsystem.entity.Comment;
import com.assignment.guardrailsystem.entity.Post;

public interface PostService {

    Post createPost(CreatePostRequest request);

    Comment addComment(Long postId, CreateCommentRequest request);

    void likePost(Long postId);
}
