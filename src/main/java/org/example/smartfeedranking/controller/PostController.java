package org.example.smartfeedranking.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.smartfeedranking.entity.interaction.Type;
import org.example.smartfeedranking.entity.post.Post;
import org.example.smartfeedranking.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "Endpoints for managing posts and interactions")
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<Post> create(@RequestParam String content) {
        return ResponseEntity.ok(postService.createPost(content));
    }

    @PostMapping("/{postId}/interact")
    public ResponseEntity<String> interact(
            @PathVariable Long postId,
            @RequestParam Type type,
            @RequestParam(required = false) String content
    ) {
        postService.interact(postId, type, content);
        return ResponseEntity.ok("Thanks for your feedback");
    }

    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getFeed() {
        return ResponseEntity.ok(postService.getFeed());
    }
}
