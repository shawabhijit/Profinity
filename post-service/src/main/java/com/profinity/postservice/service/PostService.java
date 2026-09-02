package com.profinity.postservice.service;

import com.profinity.postservice.dto.CommentResponse;
import com.profinity.postservice.dto.PostAttachmentRequest;
import com.profinity.postservice.dto.PostResponse;
import com.profinity.postservice.entity.Comment;
import com.profinity.postservice.entity.Like;
import com.profinity.postservice.entity.Post;
import com.profinity.postservice.entity.PostAttachment;
import com.profinity.postservice.exception.PostException;
import com.profinity.postservice.kafka.KafkaEventProducer;
import com.profinity.postservice.repositoty.CommentRepository;
import com.profinity.postservice.repositoty.LikeRepository;
import com.profinity.postservice.repositoty.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final S3Service s3Service;
//    private final PostAttachmentRepository postAttachmentRepository;
    private final KafkaEventProducer kafkaEventProducer;

    /**
     * Create a Post
     * Optionally upload image to S3
     * Publish post.created event to kafka
     * Consume by feed-service and search service
     * @param authorId
     * @param content
     * @param fileRequests
     * @return PostResponse
     */
    @Transactional
    public PostResponse createPost(UUID authorId , String content,
                                   List<PostAttachmentRequest> fileRequests) {
        Post post = new Post();
        post.setAuthorId(authorId);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post newPost = postRepository.save(post);

        if (fileRequests != null && !fileRequests.isEmpty()) {
            fileRequests.forEach(file -> {
                String url = s3Service.uploadFile(file.getFile(), "posts/" + authorId);

                PostAttachment attachment = new PostAttachment();
                attachment.setUrl(url);
                attachment.setFilename(file.getFileName());
                attachment.setFileSize(file.getFileSize());
                attachment.setType(file.getType());

                newPost.addAttachment(attachment); // sets post internally + adds to list
            });
        }

        // publish post.created event
        kafkaEventProducer.sentPostCreatedEvent(newPost);

        return mapToPostResponse(post);
    }

    @Transactional
    public PostResponse updatePost(UUID postId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException("Post not found with id: " + postId + ".")
        );
        post.setContent(content);
        return mapToPostResponse(post);
    }

    public PostResponse getPostById(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException("Post not found with id: " + postId + ".")
        );
        return mapToPostResponse(post);
    }

    public List<PostResponse> getAllPostsByUserID(UUID userId) {
        List<Post> posts = postRepository.findByAuthorIdOrderByCreatedAtDesc(userId);
        return posts.stream().map(this::mapToPostResponse).toList();
    }

    /**
     * delete a specific post of an author
     * should also delete attachments
     * explicitly delete comments associated with this post by postId
     * @param postId
     * @param authorId
     * @return "Post deleted successfully" with status code 200
     */
    @Transactional
    public String deletePost(UUID postId, UUID authorId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException("Post not found with id: " + postId + ".")
        );

        if(!post.getAuthorId().equals(authorId)) {
            throw new IllegalArgumentException("You are not authorized to delete this post.");
        }
        commentRepository.deleteByPostId(postId); // deletes all the comments which are having same postId
        likeRepository.deleteByPostId(postId); // same as comment
        postRepository.delete(post);

        return "Post deleted successfully";
    }

    /**
     * like or unlike post handlers starts from here
     * @param postId
     * @param userId
     * @return LikeResponse
     */
    @Transactional
    public String likePost(UUID postId, UUID userId) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException("Post not found with id: " + postId + ".")
        );

        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            post.setLikeCount(post.getLikeCount() - 1);
            return "Post unliked successfully";
        }

        Like like = new Like();
        like.setPostId(postId);
        like.setUserId(userId);
        likeRepository.save(like);
        post.setLikeCount(post.getLikeCount() + 1);

        // publish post.liked event
        kafkaEventProducer.sentPostLikeEvent(postId, userId, post.getAuthorId());

        return "Post liked successfully";
    }

    /**
     * Comment handlers starts from here
     * @param postId
     * @param userId
     * @param content
     * @return CommentResponse
     */
    @Transactional
    public CommentResponse addComment(UUID postId, UUID userId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException("Post not found with id: " + postId + ".")
        );
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setAuthorId(userId);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        comment = commentRepository.save(comment);

        post.setCommentCount(post.getCommentCount() + 1);

        //publish post.commented kafka event
        kafkaEventProducer.sentPostCommentEvent(
                postId,
                userId,
                comment.getId(),
                post.getAuthorId()
        );

        return new CommentResponse(comment.getId(), postId, content);
    }

    public List<CommentResponse> getComments(UUID postId , LocalDateTime before) {
        if(!postRepository.existsById(postId)) {
            throw new PostException("Post not found with id: " + postId + ".");
        }
        /**
         * here we are trying to fetch latest comments older then the last one i already have
         */
        Pageable pageable = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Comment> comments = (before == null)
                ? commentRepository.findByPostId(postId , pageable)
                : commentRepository.findByPostIdAndCreatedAt(postId, before, pageable);

        return comments.stream().map(
                comment ->  new CommentResponse(
                        comment.getId(),
                        comment.getPostId(),
                        comment.getContent()
                )
        ).toList();
    }

    public String deleteComment(UUID commentId, UUID userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("Comment not found with id: " + commentId + ".")
        );
        if (!comment.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }
        commentRepository.delete(comment);
        return "Comment deleted successfully";
    }

    private PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .authorId(post.getAuthorId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .attachments(post.getAttachments())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .likedByCurrentUser(post.isLikedByCurrentUser())
                .build();
    }
}
