package com.seti.franchise.model.franchise.gateway;

import com.seti.franchise.model.franchise.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(String id);
    Flux<Franchise> findAll();
    Mono<Franchise> updateName(String id, String newName);
    Mono<Franchise> findByName(String name);
}
