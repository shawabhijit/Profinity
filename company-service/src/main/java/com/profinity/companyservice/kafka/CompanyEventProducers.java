package com.profinity.companyservice.kafka;

import com.profinity.companyservice.entity.Company;
import com.profinity.companyservice.event.CompanyCreatedRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyEventProducers {

    private final KafkaTemplate<String, CompanyCreatedRecord> kafkaTemplate;

    private static final String COMPANY_CREATED_TOPIC = "company.created";
    private static final String COMPANY_UPDATED_TOPIC = "company.updated";

    public void sendCompanyCreatedEvent(Company company) {
        CompanyCreatedRecord record = new CompanyCreatedRecord(
                company.getName(),
                company.getDescription(),
                company.getOverview(),
                company.getWebsite(),
                company.getIndustry(),
                company.getAddress(),
                company.getType()
        );

        try {
            kafkaTemplate.send(COMPANY_CREATED_TOPIC, record);
            log.info("Sent company created event: {}", record);
        }
        catch (Exception e) {
            log.error("Error sending company created event: {}", e.getMessage());
            System.out.println("Error sending company created event: " + e.getMessage());
        }
    }

    public void sendCompanyUpdatedEvent(Company company) {
        CompanyCreatedRecord record = new CompanyCreatedRecord(
                company.getName(),
                company.getDescription(),
                company.getOverview(),
                company.getWebsite(),
                company.getIndustry(),
                company.getAddress(),
                company.getType()
        );

        try {
            kafkaTemplate.send(COMPANY_UPDATED_TOPIC, record);
            log.info("Sent company updated event: {}", record);
        }
        catch (Exception e) {
            log.error("Error sending company updated event: {}", e.getMessage());
            System.out.println("Error sending company created event: " + e.getMessage());
        }
    }
}
