package com.seti.franchise.mongo.franchise;

import com.seti.franchise.model.franchise.Franchise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FranchiseMongoRepositoryAdapterTest {

    private FranchiseMongoDBRepository repository;
    private ObjectMapper mapper;
    private FranchiseMongoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(FranchiseMongoDBRepository.class);
        mapper = mock(ObjectMapper.class);
        adapter = new FranchiseMongoRepositoryAdapter(repository, mapper);
    }

    @Test
    void save_should_return_saved_franchise() {
        Franchise f = Franchise.builder().id("1").name("N").build();
        FranchiseDocument d = FranchiseDocument.builder().id("1").name("N").build();
        when(repository.save(any(FranchiseDocument.class))).thenReturn(Mono.just(d));

        StepVerifier.create(adapter.save(f))
                .expectNextMatches(fr -> fr.getId().equals("1") && fr.getName().equals("N"))
                .verifyComplete();
    }

    @Test
    void findByName_maps_document_to_entity() {
        FranchiseDocument d = FranchiseDocument.builder().id("2").name("X").build();
        when(repository.findByName("X")).thenReturn(Mono.just(d));

        StepVerifier.create(adapter.findByName("X"))
                .expectNextMatches(fr -> fr.getName().equals("X") && fr.getId().equals("2"))
                .verifyComplete();
    }

    @Test
    void updateName_updates_and_returns() {
        FranchiseDocument orig = FranchiseDocument.builder().id("9").name("old").build();
        FranchiseDocument updated = orig.toBuilder().name("new").build();

        when(repository.findById("9")).thenReturn(Mono.just(orig));
        when(repository.save(any(FranchiseDocument.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(adapter.updateName("9", "new"))
                .expectNextMatches(fr -> fr.getName().equals("new") && fr.getId().equals("9"))
                .verifyComplete();

        verify(repository).save(any(FranchiseDocument.class));
    }

}
