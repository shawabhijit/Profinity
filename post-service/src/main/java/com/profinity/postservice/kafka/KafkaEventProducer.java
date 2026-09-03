package com.profinity.postservice.kafka;

import com.profinity.postservice.entity.Post;
import com.profinity.postservice.event.CommentEventRecord;
import com.profinity.postservice.event.LikeEventRecord;
import com.profinity.postservice.event.PostAttachmentRecord;
import com.profinity.postservice.event.PostCreatedRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventProducer {

    private final KafkaTemplate<String , PostCreatedRecord> kafkaPostTemplate;
    private final KafkaTemplate<String , LikeEventRecord> kafkaLikeTemplate;
    private final KafkaTemplate<String , CommentEventRecord> kafkaCommentTemplate;

    private static final String POST_CREATED_TOPIC = "post.created";
    private static final String POST_LIKED_TOPIC = "post.liked";
    private static final String POST_COMMENTED_TOPIC = "post.commented";

    public void sentPostCreatedEvent(Post post) {
        List<PostAttachmentRecord> attachmentRecords = post.getAttachments().stream()
                .map(attachment -> new PostAttachmentRecord(
                        attachment.getUrl(),
                        attachment.getFilename(),
                        attachment.getFileSize(),
                        attachment.getType().toString()
        )).toList();

        PostCreatedRecord postCreatedRecord = new PostCreatedRecord(
                post.getId(),
                post.getAuthorId(),
                post.getContent(),
                attachmentRecords,
                post.getCreatedAt()
        );

        try {
            kafkaPostTemplate.send(POST_CREATED_TOPIC, postCreatedRecord);
            log.info("Post created event sent: {}", postCreatedRecord);
        }
        catch (Exception e) {
            log.error("Error sending post created event: {}", e.getMessage());
            System.out.println("Error sending post created event: " + e.getMessage());
        }

    }

    public void sentPostLikeEvent(UUID postId, UUID userId, UUID authorId) {
        LikeEventRecord likeEventRecord = new LikeEventRecord(postId, userId, authorId);
        try {
            kafkaLikeTemplate.send(POST_LIKED_TOPIC, likeEventRecord);
            log.info("Post like event sent: {}", likeEventRecord);
        }
        catch (Exception e) {
            log.error("Error sending post like event: {}", e.getMessage());
            System.out.println("Error sending post like event: " + e.getMessage());
        }
    }

    public void sentPostCommentEvent(UUID postId, UUID userId, UUID commentId, UUID postAuthorId) {
        CommentEventRecord commentEventRecord = new CommentEventRecord(
                postId,
                userId,
                commentId,
                postAuthorId
        );

        try {
            kafkaCommentTemplate.send(POST_COMMENTED_TOPIC, commentEventRecord);
            log.info("Post comment event sent: {}", commentEventRecord);
        }
        catch (Exception e) {
            log.error("Error sending post comment event: {}", e.getMessage());
            System.out.println("Error sending post comment event: " + e.getMessage());
        }
    }
}
