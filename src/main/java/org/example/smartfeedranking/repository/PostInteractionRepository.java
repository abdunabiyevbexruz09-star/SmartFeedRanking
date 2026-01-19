package org.example.smartfeedranking.repository;

import org.example.smartfeedranking.entity.interaction.PostInteraction;
import org.example.smartfeedranking.entity.interaction.Type;
import org.example.smartfeedranking.entity.post.Post;
import org.example.smartfeedranking.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostInteractionRepository extends JpaRepository<PostInteraction, Long> {

    Optional<PostInteraction> findByPostAndUserAndType(Post post, User user, Type type);
    List<PostInteraction> findByPostAndType(Post post, Type type);
}
