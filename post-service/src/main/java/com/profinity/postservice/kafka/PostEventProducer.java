package com.profinity.postservice.kafka;

import com.profinity.postservice.entity.Post;
import com.profinity.postservice.event.PostAttachmentRecord;
import com.profinity.postservice.event.PostCreatedRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostEventProducer {

    private final KafkaTemplate<String , PostCreatedRecord> kafkaTemplate;

    private static final String POST_CREATED_TOPIC = "post.created";

    public void sentPostCreatedEvent(Post post) {
        List<PostAttachmentRecord> attachmentRecords = post.getAttachments().stream()
                .map(attachment -> new PostAttachmentRecord(
                        attachment.getUrl(),
                        attachment.getFilename(),
                        attachment.getFileSize(),
                        attachment.getType()
        )).toList();

        PostCreatedRecord postCreatedRecord = new PostCreatedRecord(
                post.getId(),
                post.getAuthorId(),
                post.getContent(),
                attachmentRecords,
                post.getCreatedAt()
        );

        try {
            kafkaTemplate.send(POST_CREATED_TOPIC, postCreatedRecord);
            log.info("Post created event sent: {}", postCreatedRecord);
        }
        catch (Exception e) {
            log.error("Error sending post created event: {}", e.getMessage());
            System.out.println("Error sending post created event: " + e.getMessage());
        }

    }
}
