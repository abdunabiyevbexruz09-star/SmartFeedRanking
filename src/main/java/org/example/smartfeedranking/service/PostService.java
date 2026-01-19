package org.example.smartfeedranking.service;

import lombok.RequiredArgsConstructor;
import org.example.smartfeedranking.dto.PostCreateDto;
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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostInteractionRepository postInteractionRepository;
    private final StringRedisTemplate redisTemplate;

    public Post createPost(String content) {
        Post post = Post.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .score(0L)
                .build();

        return postRepository.save(post);
    }

    public void interact(Long postId, Type type, String content) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostInteraction interaction = PostInteraction.builder()
                .post(post)
                .type(type)
                .content(type == Type.COMMENT ? content : null)
                .createdAt(LocalDateTime.now())
                .build();

        postInteractionRepository.save(interaction);

        long delta = (type == Type.LIKE) ? 1 : 2;

        redisTemplate.opsForValue()
                .increment("post:" + postId + ":score", delta);
    }

    public Long getScore(Long postId) {
        String value = redisTemplate.opsForValue()
                .get("post:" + postId + ":score");

        return value == null ? 0L : Long.parseLong(value);
    }


    public List<Post> getFeed() {
        LocalDateTime from = LocalDateTime.now().minusHours(24);
        return postRepository.findByCreatedAtAfterOrderByScoreDesc(from);
    }
}
