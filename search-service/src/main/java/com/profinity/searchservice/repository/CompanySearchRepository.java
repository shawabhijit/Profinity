package com.profinity.searchservice.repository;

import com.profinity.searchservice.model.CompanyDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface CompanySearchRepository extends ElasticsearchRepository<CompanyDocument, UUID> {
}
