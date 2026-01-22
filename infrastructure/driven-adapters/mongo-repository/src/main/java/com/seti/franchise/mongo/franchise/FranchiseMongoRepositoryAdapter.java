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

    @Override
    public Mono<Franchise> findByName(String name) {
        return this.repository.findByName(name)
                .map(this::toEntity);
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return this.repository.save(toData(franchise))
                .map(this::toEntity);
    }

    @Override
    protected FranchiseDocument toData(Franchise model) {
        return FranchiseDocument.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    @Override
    protected Franchise toEntity(FranchiseDocument document) {
        return Franchise.builder()
                .id(document.getId())
                .name(document.getName())
                .build();
    }


}
