package com.profinity.searchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostAttachment {
    private String url;
    @Field(type = FieldType.Keyword)
    private String fileName;
    private long fileSize;
    private String type;
}
