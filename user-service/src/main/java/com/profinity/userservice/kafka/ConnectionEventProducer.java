package com.profinity.userservice.kafka;

import com.profinity.userservice.entity.Connection;
import com.profinity.userservice.event.ConnectionRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionEventProducer {

    private final KafkaTemplate<String, ConnectionRecord> kafkaTemplate;

    private static final String CONNECTION_REQUEST_TOPIC = "connection.request";
    private static final String CONNECTION_ACCEPTED_TOPIC = "connection.accepted";
    private static final String CONNECTION_REJECTED_TOPIC = "connection.rejected";

    public void sendConnectionRequestEvent(Connection connection) {
        ConnectionRecord connectionRecord = new ConnectionRecord(
                connection.getRequesterId(),
                connection.getReceiverId(),
                connection.getStatus()
        );
        try {
            kafkaTemplate.send(CONNECTION_REQUEST_TOPIC, connectionRecord);
            log.info("Connection request event sent: {}", connectionRecord);
        }
        catch (KafkaException ke) {
            System.out.println("KafkaException while sending connection request event from user: " + ke.getMessage());
            log.error("KafkaException while sending connection request event from user: {}", ke.getMessage());
        }
    }

    public void sendConnectionAcceptedEvent(Connection connection) {
        ConnectionRecord connectionRecord = new ConnectionRecord(
                connection.getRequesterId(),
                connection.getReceiverId(),
                connection.getStatus()
        );
        try {
            kafkaTemplate.send(CONNECTION_ACCEPTED_TOPIC, connectionRecord);
            log.info("Connection accepted event sent: {}", connectionRecord);
        }
        catch (KafkaException ke) {
            System.out.println("KafkaException while sending connection accepted event from user: " + ke.getMessage());
            log.error("KafkaException while sending connection accepted event from user: {}", ke.getMessage());
        }
    }

    public void sendConnectionRejectedEvent(Connection connection) {
        ConnectionRecord connectionRecord = new ConnectionRecord(
                connection.getRequesterId(),
                connection.getReceiverId(),
                connection.getStatus()
        );
        try {
            kafkaTemplate.send(CONNECTION_REJECTED_TOPIC, connectionRecord);
            log.info("Connection rejected event sent: {}", connectionRecord);
        }
        catch (KafkaException ke) {
            System.out.println("KafkaException while sending connection rejected event from user: " + ke.getMessage());
            log.error("KafkaException while sending connection rejected event from user: {}", ke.getMessage());
        }
    }
}
