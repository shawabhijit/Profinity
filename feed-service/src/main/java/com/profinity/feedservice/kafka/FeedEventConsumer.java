package com.profinity.feedservice.kafka;

import com.profinity.feedservice.client.UserServiceClient;
import com.profinity.feedservice.event.PostCreatedRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedEventConsumer {

    private final UserServiceClient userServiceClient;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String FEED_KEY_PREFIX = "feed:";

    @Value( "${feed.max-size}")
    private int maxFeedSize;

    /**
     * consume post.created event
     * when a user creates a post - immediately push that post
     * to all their connections feed
     *
     * this is how Linkedin/ X generated feed works
     * @param postCreatedRecord
     */
    @KafkaListener(topics = "post.created" , groupId = "feed-service")
    public void consumePostCreatedEvent(PostCreatedRecord postCreatedRecord) {

        try {
            UUID postId = postCreatedRecord.postId();
            UUID authorId = postCreatedRecord.authorId();

            List<Map<String, Object>> connections = userServiceClient.getConnections(authorId);

            //push post to each connection feed
            for (Map<String, Object> connection : connections) {
                UUID connectionId = UUID.fromString(connection.get("id").toString());
                String feedKey = FEED_KEY_PREFIX + connectionId;
                
                redisTemplate.opsForZSet()
                        .add(
                                feedKey,
                                postId.toString(),
                                postCreatedRecord.createdAt().toEpochSecond(ZoneOffset.UTC)
                        );

                redisTemplate.opsForZSet().removeRange(
                        feedKey,
                        0,
                        -(maxFeedSize + 1) // Count the position from the newest/end of the sorted set
                );

                log.info("Post {} added to feed of user {}", postId, connectionId);
            }

            // also pushed to author own feed as Linkedin/ X
            String feedKey = FEED_KEY_PREFIX + authorId;
            redisTemplate.opsForZSet()
                    .add(
                            feedKey,
                            postId.toString(),
                            postCreatedRecord.createdAt().toEpochSecond(ZoneOffset.UTC)
                    );
            // trimming to max size
            redisTemplate.opsForZSet().removeRange(
                    feedKey,
                    0,
                    -(maxFeedSize + 1)
            );

            log.info("Post created event consumed: {}", postCreatedRecord);
        }
        catch (Exception e) {
            log.error("Error while consuming post.created event: {}", e.getMessage());
            throw new RuntimeException("Error while consuming post.created event: " +  e.getMessage());
        }

    }
}
