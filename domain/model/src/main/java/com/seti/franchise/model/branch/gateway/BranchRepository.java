package com.seti.franchise.model.branch.gateway;

import com.seti.franchise.model.branch.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {

    Mono<Branch> save(Branch branch);
    Mono<Branch> findById(String id);
    Mono<Branch> updateName(String id, String newName);
    Mono<Branch> findByNameAndFranchiseId(String name, String franchiseId);
    Flux<Branch> findByFranchiseId(String franchiseId);
    Mono<Branch> findByName(String name);

}
