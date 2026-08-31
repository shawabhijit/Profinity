package com.profinity.postservice.service;

import com.profinity.postservice.dto.CommentResponse;
import com.profinity.postservice.dto.LikeResponse;
import com.profinity.postservice.dto.PostRequest;
import com.profinity.postservice.dto.PostResponse;
import com.profinity.postservice.repositoty.CommentRepository;
import com.profinity.postservice.repositoty.LikeRepository;
import com.profinity.postservice.repositoty.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public PostResponse createPost(UUID authorId , PostRequest postRequest,
                                   List<MultipartFile> files) {
        return null;
    }

    public PostResponse updatePost(UUID postId, String content) {
        return null;
    }

    public PostResponse getPostById(UUID postId) {
        return null;
    }

    public List<PostResponse> getAllPostsByUserID(UUID userId) {
        return null;
    }

    public String deletePost(UUID postId, UUID userId) {
        return null;
    }

    public LikeResponse likePost(UUID postId, UUID userId) {
        return null;
    }

    public CommentResponse addComment(UUID postId, UUID userId, String comment) {
        return null;
    }

    public List<CommentResponse> getComments(UUID postId) {
        return null;
    }
}
