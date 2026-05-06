package com.assignment.guardrailsystem.service.impl;

import com.assignment.guardrailsystem.dto.CreateCommentRequest;
import com.assignment.guardrailsystem.dto.CreatePostRequest;
import com.assignment.guardrailsystem.entity.Comment;
import com.assignment.guardrailsystem.entity.Post;
import com.assignment.guardrailsystem.repository.CommentRepository;
import com.assignment.guardrailsystem.repository.PostRepository;
import com.assignment.guardrailsystem.service.PostService;
import com.assignment.guardrailsystem.service.ViralityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ViralityService viralityService;

    @Override
    public Post createPost(CreatePostRequest request) {

        Post post = Post.builder()
                .authorId(request.getAuthorId())
                .authorType(request.getAuthorType())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        return postRepository.save(post);
    }

    @Override
    public Comment addComment(Long postId, CreateCommentRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        int depth = 1;
        Comment parentComment = null;

        if (request.getParentCommentId() != null) {

            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));

            depth = parentComment.getDepthLevel() + 1;
        }

        if (depth > 20) {
            throw new RuntimeException("Maximum reply depth exceeded");
        }

        int score = request.getAuthorType().name().equals("BOT")
                ? 1
                : 50;

        if (request.getAuthorType().name().equals("BOT")) {

            Long botCount = viralityService.incrementBotCount(postId);

            if (botCount > 99) {
                throw new IllegalStateException("Bot reply limit exceeded for this post");
            }

            Long humanUserId = post.getAuthorId();

            boolean cooldownCreated = viralityService.createCooldown(
                    request.getAuthorId(),
                    humanUserId
            );

            if (!cooldownCreated) {
                throw new IllegalStateException(
                        "Bot is in cooldown period for this user"
                );
            }
        }

        Comment comment = Comment.builder()
                .post(post)
                .parentComment(parentComment)
                .authorId(request.getAuthorId())
                .authorType(request.getAuthorType())
                .content(request.getContent())
                .depthLevel(depth)
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);

        viralityService.increaseVirality(postId, score);

        if (request.getAuthorType().name().equals("BOT")) {

            viralityService.handleNotification(
                    post.getAuthorId(),
                    "Bot " + request.getAuthorId() + " replied to your post"
            );
        }

        return savedComment;
    }
    @Override
    public void likePost(Long postId) {

        postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        viralityService.increaseVirality(postId, 20);

        System.out.println("Post liked: " + postId);
    }
}