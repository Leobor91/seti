package com.seti.franchise.mongo.product;

import com.seti.franchise.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductMongoRepositoryAdapterTest {

    private ProductMongoDBRepository repository;
    private ObjectMapper mapper;
    private productMongoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(ProductMongoDBRepository.class);
        mapper = mock(ObjectMapper.class);
        adapter = new productMongoRepositoryAdapter(repository, mapper, d -> Product.builder()
                .id(d.getId())
                .branchId(d.getBranchId())
                .name(d.getName())
                .stock(d.getStock())
                .build());
    }

    @Test
    void updateStock_updates_and_returns() {
        ProductDocument orig = ProductDocument.builder().id("p1").branchId("b").name("N").stock(10L).build();
        ProductDocument updated = orig.toBuilder().stock(20L).build();

        when(repository.findById("p1")).thenReturn(Mono.just(orig));
        when(repository.save(any(ProductDocument.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(adapter.updateStock("p1", 20L))
                .expectNextMatches(p -> p.getStock().equals(20L) && p.getId().equals("p1"))
                .verifyComplete();
    }

    @Test
    void findFirstByBranchId_returns_entity() {
        ProductDocument d = ProductDocument.builder().id("p2").branchId("b2").name("X").stock(50L).build();
        when(repository.findFirstByBranchIdOrderByStockDesc("b2")).thenReturn(Mono.just(d));

        StepVerifier.create(adapter.findFirstByBranchIdOrderByStockDesc("b2"))
                .expectNextMatches(p -> p.getId().equals("p2") && p.getStock().equals(50L))
                .verifyComplete();
    }

}
