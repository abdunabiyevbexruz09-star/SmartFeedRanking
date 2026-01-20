package org.example.smartfeedranking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.smartfeedranking.entity.interaction.PostInteraction;
import org.example.smartfeedranking.entity.interaction.Type;
import org.example.smartfeedranking.entity.post.Post;
import org.example.smartfeedranking.repository.PostInteractionRepository;
import org.example.smartfeedranking.repository.PostRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostInteractionRepository interactionRepository;
    private final StringRedisTemplate redisTemplate;

    public Post createPost(String content) {
        Post post = Post.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .score(0L)
                .build();

        return postRepository.save(post);
    }


    @Transactional
    public void interact(Long postId, Type type, String content, String clientId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String scoreKey = "post:" + postId + ":score";
        String likeSetKey = "post:" + postId + ":likes";

        if (type == Type.LIKE) {

            Boolean alreadyLiked =
                    redisTemplate.opsForSet().isMember(likeSetKey, clientId);

            if (Boolean.TRUE.equals(alreadyLiked)) {
                redisTemplate.opsForSet().remove(likeSetKey, clientId);
                redisTemplate.opsForValue().decrement(scoreKey, 1);
                return;
            }

            redisTemplate.opsForSet().add(likeSetKey, clientId);
            redisTemplate.opsForValue().increment(scoreKey, 1);
            return;
        }

        PostInteraction comment = PostInteraction.builder()
                .post(post)
                .type(Type.COMMENT)
                .content(content)
                .build();

        interactionRepository.save(comment);
        redisTemplate.opsForValue().increment(scoreKey, 2);
    }



    public Long getScore(Long postId) {
        String score = redisTemplate.opsForValue()
                .get("post:" + postId + ":score");

        return score == null ? 0L : Long.parseLong(score);

    }

    public List<Post> getFeed() {
        LocalDateTime from = LocalDateTime.now().minusHours(24);
        return postRepository.findByCreatedAtAfterOrderByScoreDesc(from);
    }
}
