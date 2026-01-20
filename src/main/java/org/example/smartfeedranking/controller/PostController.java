package org.example.smartfeedranking.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.smartfeedranking.dto.PostCreateDto;
import org.example.smartfeedranking.entity.interaction.PostInteraction;
import org.example.smartfeedranking.entity.interaction.Type;
import org.example.smartfeedranking.entity.post.Post;
import org.example.smartfeedranking.repository.PostRepository;
import org.example.smartfeedranking.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @PostMapping("/create")
    public ResponseEntity<Post> create(@RequestParam String content) {
        return ResponseEntity.ok(postService.createPost(content));
    }

    @PostMapping("/{postId}/interact")
    public ResponseEntity<String> interact(@PathVariable Long postId,
                                           @RequestParam Type type,
                                           @RequestParam(required = false) String content,
                                           HttpServletRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String clientId = request.getRemoteAddr();
        postService.interact(postId, type, content, clientId);

        return ResponseEntity.ok("Thanks for your feedback");
    }

    private String getClientId(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getFeed() {
        return ResponseEntity.ok(postService.getFeed());
    }
}
