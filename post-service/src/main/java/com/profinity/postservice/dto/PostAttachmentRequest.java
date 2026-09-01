package com.profinity.postservice.dto;

import com.profinity.postservice.entity.enums.AttachmentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostAttachmentRequest {
    @NotNull(message = "File name should not be null")
    private String fileName;
    @NotNull(message = "File size should not be null")
    private long fileSize;
    @NotNull(message = "File should not be null")
    private MultipartFile file;

    private AttachmentType type;
}
