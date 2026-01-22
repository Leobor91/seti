package com.seti.franchise.mongo.product;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface ProductMongoDBRepository extends ReactiveMongoRepository<ProductDocument, String>, ReactiveQueryByExampleExecutor<ProductDocument> {

    Mono<ProductDocument> findByName(String name);

}
