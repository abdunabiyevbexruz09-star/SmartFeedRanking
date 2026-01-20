package org.example.smartfeedranking.schedule;

import lombok.RequiredArgsConstructor;
import org.example.smartfeedranking.entity.post.Post;
import org.example.smartfeedranking.repository.PostRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GlobalSchedular {

    private final RedisTemplate<String, String> redisTemplate;
    private final PostRepository postRepository;

    @Scheduled(fixedRate = 20 * 1000)
    public void flushScoreFromRedisToDb() {

        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            String key = "post:" + post.getId() + ":score";
            String redisValue = redisTemplate.opsForValue().get(key);

            if (redisValue == null) continue;

            long redisScore = Long.parseLong(redisValue);
            post.setScore(post.getScore() + redisScore);

            postRepository.save(post);
            redisTemplate.delete(key);
        }
    }
}
