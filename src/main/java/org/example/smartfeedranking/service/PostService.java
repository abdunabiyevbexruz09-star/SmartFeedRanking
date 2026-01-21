package org.example.smartfeedranking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.smartfeedranking.entity.interaction.PostInteraction;
import org.example.smartfeedranking.entity.interaction.Type;
import org.example.smartfeedranking.entity.post.Post;
import org.example.smartfeedranking.entity.user.User;
import org.example.smartfeedranking.repository.PostInteractionRepository;
import org.example.smartfeedranking.repository.PostRepository;
import org.example.smartfeedranking.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostInteractionRepository interactionRepository;
    private final RedisTemplate<String, Long> redisTemplate;

    public Post createPost(String content) {
        Post post = Post.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .score(0L)
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public void interact(Long postId, Type type, String content) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostInteraction interaction = PostInteraction.builder()
                .post(post)
                .type(type)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        interactionRepository.save(interaction);

        String key = "post:" + postId + ":score" + post.getScore();

        long delta = switch (type) {
            case LIKE -> 1;
            case COMMENT -> 2;
        };

        redisTemplate.opsForValue().increment(key, delta);
    }

    public List<Post> getFeed() {
        LocalDateTime from = LocalDateTime.now().minusHours(24);

        return postRepository.findByCreatedAtAfterOrderByScoreDesc(from);
    }
}
