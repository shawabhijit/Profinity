package com.profinity.userservice.kafka;

import com.profinity.userservice.event.UserCreatedEvent;
import com.profinity.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final UserService userService;

    @KafkaListener(topics = "authuser.created" , groupId = "user-service")
    public void consumeUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        log.info("Received user created event: {}", userCreatedEvent);
        userService.createUser(userCreatedEvent);
    }
}
