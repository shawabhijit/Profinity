package com.profinity.userservice.kafka;

import com.profinity.userservice.entity.User;
import com.profinity.userservice.event.UserEventRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, UserEventRecord> kafkaTemplate;

    private static final String USER_UPDATED_TOPIC = "user.updated";

    public void sendUserUpdatedEvent(User user) {
        UserEventRecord record = new UserEventRecord(
                user.getId(),
                user.getUsername(),
                user.getHeadline(),
                user.getLocation(),
                user.getSkills()
        );

        try {
            kafkaTemplate.send(USER_UPDATED_TOPIC, record);
            log.info("User updated event sent: {}", record);
        }
        catch (KafkaException ke) {
            System.out.println("KafkaException while sending user updated event: " + ke.getMessage());
            log.error("KafkaException while sending user updated event: {}", ke.getMessage());
        }
    }
}
