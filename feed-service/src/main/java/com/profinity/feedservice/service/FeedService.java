package com.profinity.feedservice.service;

import com.profinity.feedservice.dto.CursorData;
import com.profinity.feedservice.dto.FeedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String FEED_KEY_PREFIX = "feed:";

    public FeedResponse getFeed(UUID userId, String cursor , int size) {

        log.info("Getting feed for user: {}, cursor: {}, size: {}", userId, cursor, size);

        if(size < 1 || size > 50) {
            throw new IllegalArgumentException("Size must be between 1 and 50");
        }

        String feedKey = FEED_KEY_PREFIX + userId;

        CursorData cursorData = decodeCursor(cursor);

        Set<Object> postIdsSet;

        if(cursorData == null) {
            // first page
            // from the users entire feed, give me up to "size" posts, starting form the newest post.
            postIdsSet = redisTemplate.opsForZSet().reverseRangeByScore(
                    feedKey,
                    Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY,
                    0,
                    size
            );
        }
        else {
            // next page
            // Don't give me the post represented by the cursor or anything newer, Give me posts strictly older than
            // the cursor
            postIdsSet = redisTemplate.opsForZSet().reverseRangeByScore(
                    feedKey,
                    Double.NEGATIVE_INFINITY,
                    cursorData.timestamp() -1,
                    0,
                    size + 1
            );
        }

        if(postIdsSet == null || postIdsSet.isEmpty()) {
            return new FeedResponse(
                    new ArrayList<>(),
                    null,
                    false
            );
        }

        List<Object> postIds = new ArrayList<>(postIdsSet);

        boolean hasNext = postIds.size() > size;

        if(hasNext) {
            postIds = postIds.subList(0, size);
        }

        List<UUID> ids = postIds.stream()
                .map(id -> UUID.fromString(id.toString())).toList();

        String nextCursor = null;

        if(hasNext) {
            UUID lastId = ids.get(ids.size() - 1);

            Double lastScore = redisTemplate.opsForZSet().score(feedKey, lastId.toString());

            if(lastScore != null) {
                nextCursor = encodeCursor(lastScore.longValue(), lastId);
            }
        }

        return new FeedResponse(ids, nextCursor, hasNext);
    }

    public FeedResponse clearFeed(UUID userId) {
        String feedKey = FEED_KEY_PREFIX + userId;
        redisTemplate.delete(feedKey);
        log.info("Cleared feed for user: {}", userId);
        return new FeedResponse(new ArrayList<>(), null, false);
    }

    private String encodeCursor(long timestamp, UUID id) {
        String cursorData = timestamp + ":" + id;
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(
                        cursorData.getBytes()
                );
    }

    private CursorData decodeCursor(String cursor) {
        if(cursor == null || cursor.isBlank())  {
            return null;
        }

        try {
            String decode = new String(
                    Base64.getUrlDecoder().decode(cursor),
                    StandardCharsets.UTF_8
            );

            String[] parts = decode.split(":" , 2);

            if(parts.length != 2) {
                throw new IllegalArgumentException("Invalid cursor format");
            }

            long timestamp = Long.parseLong(parts[0]);
            UUID id = UUID.fromString(parts[1]);

            return new CursorData(timestamp, id);
        }
        catch (Exception e) {
            log.warn("Inavlid cursor format: {}", cursor);
            throw new IllegalArgumentException("Invalid feed cursor ", e);
        }
    }
}
