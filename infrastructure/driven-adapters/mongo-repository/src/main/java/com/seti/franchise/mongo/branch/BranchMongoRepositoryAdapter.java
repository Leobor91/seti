package com.seti.franchise.mongo.branch;

import com.seti.franchise.model.branch.Branch;
import com.seti.franchise.model.branch.gateway.BranchRepository;
import com.seti.franchise.model.product.gateway.ProductRepository;
import com.seti.franchise.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Repository
public class BranchMongoRepositoryAdapter extends AdapterOperations<Branch, BranchDocument, String, BranchMongoDBRepository> implements BranchRepository {


    protected BranchMongoRepositoryAdapter(BranchMongoDBRepository repository, ObjectMapper mapper, Function<BranchDocument, Branch> toEntityFn) {
        super(repository, mapper, toEntityFn);
    }

    @Override
    public Flux<Branch> findByFranchiseId(String franchiseId) {
        return this.repository.findByFranchiseId(franchiseId)
                .map(this::toEntity);
    }

    @Override
    public Mono<Branch> updateName(String id, String newName) {
        return this.repository.findById(id)
                .flatMap(branchDocument  ->
                        this.repository.save(branchDocument.toBuilder()
                                .name(newName)
                                .build()))
                .map(this::toEntity);
    }
}
