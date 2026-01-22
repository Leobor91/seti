package com.seti.franchise.mongo.franchise;

import com.seti.franchise.model.franchise.Franchise;
import com.seti.franchise.model.franchise.gateway.FranchiseRepository;
import com.seti.franchise.mongo.helper.AdapterOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseMongoRepositoryAdapter extends AdapterOperations<Franchise, FranchiseDocument, String, FranchiseMongoDBRepository> implements FranchiseRepository {

    public FranchiseMongoRepositoryAdapter(FranchiseMongoDBRepository repository, org.reactivecommons.utils.ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
    }

    @Override
    public Mono<Franchise> updateName(String id, String newName) {
        return this.repository.findById(id)
                .flatMap(franchiseDocument ->
                        this.repository.save(franchiseDocument.toBuilder()
                                .name(newName)
                                .build()))
                .map(this::toEntity);
    }
}
