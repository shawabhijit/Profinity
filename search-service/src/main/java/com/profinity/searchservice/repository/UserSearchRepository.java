package com.profinity.searchservice.repository;

import com.profinity.searchservice.model.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, UUID> {
}
