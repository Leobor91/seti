package com.seti.franchise.mongo.config;

import com.seti.franchise.model.franchise.Franchise;
import com.seti.franchise.mongo.franchise.FranchiseDocument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class FranchiseMongoConfig {

    @Bean
    public Function<FranchiseDocument, Franchise> franchiseDocumentToFranchise(){
        return document -> Franchise.builder()
                .id(document.getId())
                .name(document.getName())
                .build();
    }
}
