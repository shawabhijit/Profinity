package com.profinity.authservice.event;

import com.profinity.authservice.Entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public void sendUserCreatedEvent(User user) {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getCreatedAt()
        );
        try {
            kafkaTemplate.send(
                    "authuser.created",
                    userCreatedEvent
            );
            log.info("User created event sent: {}", userCreatedEvent);
        }
        catch (KafkaException e) {
            System.out.println("KafkaException while sending user created event from auth: " + e.getMessage());
            log.error("KafkaException while sending user created event from auth: {}", e.getMessage());
        }
    }
}
