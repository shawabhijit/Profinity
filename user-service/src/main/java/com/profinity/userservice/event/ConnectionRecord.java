package com.profinity.userservice.event;

import com.profinity.userservice.entity.enums.ConnectionStatus;

import java.util.UUID;

public record ConnectionRecord(
    UUID requesterId,
    UUID receiverId,
    ConnectionStatus status
) {
}
