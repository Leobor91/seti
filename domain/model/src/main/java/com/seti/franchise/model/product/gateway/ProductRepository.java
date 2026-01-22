package com.seti.franchise.model.product.gateway;

import com.seti.franchise.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Product> save(Product product);
    Mono<Product> findById(String id);
    Mono<Product> updateStock(String id, Long newStock);
    Mono<Product> updateName(String id, String newName);
    Mono<Void> deleteById(String id);
    Mono<Product> findByName(String name);
}
