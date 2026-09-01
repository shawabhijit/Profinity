package com.profinity.postservice.controller;

import com.profinity.postservice.dto.CommentResponse;
import com.profinity.postservice.dto.PostAttachmentRequest;
import com.profinity.postservice.dto.PostResponse;
import com.profinity.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestParam UUID authorId,
            @RequestBody String content,
            @RequestParam(required = false) List<PostAttachmentRequest> fileRequests
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                postService.createPost(authorId, content, fileRequests)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<PostResponse> updatePost(
            @RequestParam UUID postId,
            @RequestParam String content
    ) {
        return ResponseEntity.ok().body(postService.updatePost(postId, content));
    }

    @GetMapping("{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID postId) {
        return ResponseEntity.ok().body(postService.getPostById(postId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getAllUserPosts(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(postService.getAllPostsByUserID(userId));
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable UUID postId,
            @RequestParam UUID userId) {
        return ResponseEntity.ok().body(postService.deletePost(postId, userId));
    }

    /**
     *
     * like endPoints
     * start from here
     */
    @PostMapping("/{postId}/likes")
    public ResponseEntity<String> likePost(
            @PathVariable UUID postId,
            @RequestParam UUID userId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.likePost(postId, userId));
    }

    // TODO: should create get handler for get all the users who liked in a post.

    /**
     *
     * comment endPoints
     * start from here
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable UUID postId,
            @RequestParam UUID authorId,
            @RequestParam String content
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.addComment(postId, authorId, content));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable UUID postId
    ) {
        return ResponseEntity.ok().body(postService.getComments(postId));
    }

    @DeleteMapping("/delete/{commentId}/comments")
    public ResponseEntity<String> deleteComment(
            @PathVariable UUID commentId,
            @RequestParam UUID userId
    ) {
        return ResponseEntity.ok().body(postService.deleteComment(commentId, userId));
    }
}
