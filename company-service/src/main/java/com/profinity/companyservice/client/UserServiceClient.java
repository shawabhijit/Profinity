package com.profinity.companyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "user-service" , url = "${user.service.url}")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/batch")
    List<Map<String, Object>> getUsers(List<UUID> userIds);
}
