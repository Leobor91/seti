package com.seti.franchise.mongo.product;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface ProductMongoDBRepository extends ReactiveMongoRepository<ProductDocument, String>, ReactiveQueryByExampleExecutor<ProductDocument> {
}
