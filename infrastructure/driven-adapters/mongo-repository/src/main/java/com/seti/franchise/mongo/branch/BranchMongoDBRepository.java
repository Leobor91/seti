package com.seti.franchise.mongo.branch;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchMongoDBRepository extends ReactiveMongoRepository<BranchDocument, String>, ReactiveQueryByExampleExecutor<BranchDocument> {

    Mono<BranchDocument> findByNameAndFranchiseId(String name, String franchiseId);

    Flux<BranchDocument> findByFranchiseId(String franchiseId);

    Mono<BranchDocument> findByName(String name);
}
