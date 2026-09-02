package com.profinity.feedservice.controller;

import com.profinity.feedservice.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param page
     * @param size
     * @return List<UUID> ids;
     */
    public ResponseEntity<List<UUID>> getFeed(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

    }
}
