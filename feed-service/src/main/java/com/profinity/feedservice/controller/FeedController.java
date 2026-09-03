package com.profinity.feedservice.controller;

import com.profinity.feedservice.dto.FeedResponse;
import com.profinity.feedservice.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feed")
@Slf4j
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    /**
     * Get paginated feed for a user
     * Return list of post ids
     * Client fetched full post details from post service
     * @param userId
     * @param cursor
     * @param size
     * @return List<UUID> ids;
     */
    @GetMapping("/{userId}")
    public ResponseEntity<FeedResponse> getFeed(
            @PathVariable UUID userId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(feedService.getFeed(userId, cursor, size));
    }


    /**
     * clear feed cache - useful for testing
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}/cache")
    public ResponseEntity<FeedResponse> clearFeed(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok().body(feedService.clearFeed(userId));
    }
}
