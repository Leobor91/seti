package com.seti.franchise.mongo.config;

import com.seti.franchise.model.branch.Branch;
import com.seti.franchise.mongo.branch.BranchDocument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class BranchMongoConfig {

    @Bean
    public Function<BranchDocument, Branch> branchDocumentToBranch() {
        return doc ->  Branch.builder()
                .id(doc.getId())
                .name(doc.getName())
                .franchiseId(doc.getFranchiseId())
                .build();

    }
}
