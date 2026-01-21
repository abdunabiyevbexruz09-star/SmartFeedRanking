package org.example.smartfeedranking.entity.interaction;

import jakarta.persistence.*;
import lombok.*;
import org.example.smartfeedranking.entity.post.Post;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_interaction")
public class PostInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String content;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
