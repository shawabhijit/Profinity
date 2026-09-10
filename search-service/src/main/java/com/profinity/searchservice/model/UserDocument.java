package com.profinity.searchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.UUID;


/**
 *
 * Document for user search
 * Indexed when user registers or updates profile
 * Enables full text search across name, headline, skills and location
 */

@Document(indexName = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Text)
    private String username;

    @Field(type = FieldType.Text)
    private String headline;

    @Field(type = FieldType.Keyword)
    private String location;

    @Field(type = FieldType.Keyword)
    private List<String> skills;

    @Field(type = FieldType.Nested)
    private List<Education> educations;
}
