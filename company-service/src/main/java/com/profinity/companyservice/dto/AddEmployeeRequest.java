package com.profinity.companyservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddEmployeeRequest {
    @NotNull(message = "userIds should not be completely NULL, need to have at least one value.")
    List<UUID> userIds;
}
