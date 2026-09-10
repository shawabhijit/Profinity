package com.profinity.searchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(indexName = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private UUID authorId;

    @Field(type = FieldType.Text)
    private String content;

    private List<PostAttachment> attachments;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;
}
