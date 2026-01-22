package com.seti.franchise.mongo.franchise;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface FranchiseMongoDBRepository  extends ReactiveMongoRepository<FranchiseDocument, String>, ReactiveQueryByExampleExecutor<FranchiseDocument> {

    Mono<FranchiseDocument> findByName(String name);

}
