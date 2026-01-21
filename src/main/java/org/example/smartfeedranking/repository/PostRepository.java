package org.example.smartfeedranking.repository;

import org.example.smartfeedranking.entity.post.Post;
import org.example.smartfeedranking.entity.user.User;
import org.example.smartfeedranking.entity.interaction.PostInteraction;
import org.example.smartfeedranking.entity.interaction.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCreatedAtAfterOrderByScoreDesc(LocalDateTime from);
}
