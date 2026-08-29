package com.profinity.userservice.kafka;

import com.profinity.userservice.entity.User;
import com.profinity.userservice.event.EducationEvent;
import com.profinity.userservice.event.UserCreatedEvent;
import com.profinity.userservice.event.UserEventRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, UserEventRecord> kafkaTemplate;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate2;

    private static final String USER_CREATED_TOPIC = "user.created";
    private static final String USER_UPDATED_TOPIC = "user.updated";


    public void sendUserCreatedEvent(User user) {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(
                user.getId(),
                null,
                user.getUsername(),
                user.getCreatedAt()
        );

        try {
            kafkaTemplate2.send(USER_CREATED_TOPIC, userCreatedEvent);
            log.info("User created event sent: {}", userCreatedEvent);
        }
        catch (KafkaException ke) {
            System.out.println("KafkaException while sending user created event: " + ke.getMessage());
            log.error("KafkaException while sending user created event: {}", ke.getMessage());
        }
    }

    public void sendUserUpdatedEvent(User user) {
        List<EducationEvent> educationEvents = user.getEducations().stream().map(
                education -> {
                    return new EducationEvent(
                            education.getId(),
                            education.getSchool(),
                            education.getDegree(),
                            education.getFieldOfStudy()
                    );
                }
        ).toList();

        UserEventRecord record = new UserEventRecord(
                user.getId(),
                user.getUsername(),
                user.getHeadline(),
                user.getLocation(),
                user.getSkills(),
                educationEvents
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
