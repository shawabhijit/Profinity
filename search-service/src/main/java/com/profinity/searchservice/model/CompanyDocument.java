package com.profinity.searchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Document(indexName = "companies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDocument {
    @Id
    private UUID id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    String overview;

    private String website;

    @Field(type = FieldType.Keyword)
    private String industry;

    @Field(type = FieldType.Keyword)
    String address;

    @Field(type = FieldType.Keyword)
    private String companyType;
}
