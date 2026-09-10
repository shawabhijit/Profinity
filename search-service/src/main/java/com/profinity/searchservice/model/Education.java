package com.profinity.searchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    private UUID id;

    @Field(type = FieldType.Keyword)
    private String school;

    @Field(type = FieldType.Keyword)
    private String degree;

    @Field(type = FieldType.Keyword)
    private String fieldOfStudy;
}
