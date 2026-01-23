package com.seti.franchise.mongo.branch;

import com.seti.franchise.model.branch.Branch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BranchMongoRepositoryAdapterTest {

    private BranchMongoDBRepository repository;
    private ObjectMapper mapper;
    private BranchMongoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(BranchMongoDBRepository.class);
        mapper = mock(ObjectMapper.class);
        adapter = new BranchMongoRepositoryAdapter(repository, mapper, d -> Branch.builder()
                .id(d.getId())
                .franchiseId(d.getFranchiseId())
                .name(d.getName())
                .build());
    }

    @Test
    void findByFranchiseId_returns_list() {
        BranchDocument d1 = BranchDocument.builder().id("1").franchiseId("f1").name("A").build();
        BranchDocument d2 = BranchDocument.builder().id("2").franchiseId("f1").name("B").build();
        when(repository.findByFranchiseId("f1")).thenReturn(Flux.just(d1, d2));

        StepVerifier.create(adapter.findByFranchiseId("f1"))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void updateName_updates_and_returns() {
        BranchDocument orig = BranchDocument.builder().id("b1").franchiseId("f").name("old").build();
        BranchDocument updated = orig.toBuilder().name("new").build();

        when(repository.findById("b1")).thenReturn(Mono.just(orig));
        when(repository.save(any(BranchDocument.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(adapter.updateName("b1", "new"))
                .expectNextMatches(b -> b.getName().equals("new") && b.getId().equals("b1"))
                .verifyComplete();
    }

}
