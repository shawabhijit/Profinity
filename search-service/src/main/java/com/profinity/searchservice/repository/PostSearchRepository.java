package com.profinity.searchservice.repository;

import com.profinity.searchservice.model.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, UUID> {
}
