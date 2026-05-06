package com.assignment.guardrailsystem.entity;
import com.assignment.guardrailsystem.enums.AuthorType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    private Long authorId;

    @Enumerated(EnumType.STRING)
    private AuthorType authorType;

    @Column(length = 2000)
    private String content;

    private Integer depthLevel;

    private LocalDateTime createdAt;
}
