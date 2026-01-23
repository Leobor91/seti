package com.seti.franchise.mongo.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdapterOperationsTest {

    private TestRepo repository;
    private ObjectMapper mapper;
    private TestAdapter adapter;

    static class TestEntity {
        String id;
        String name;

        TestEntity(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class TestDocument {
        String id;
        String name;

        TestDocument(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    interface TestRepo extends ReactiveCrudRepository<TestDocument, String>, ReactiveQueryByExampleExecutor<TestDocument> {

    }

    static class TestAdapter extends AdapterOperations<TestEntity, TestDocument, String, TestRepo> {
        protected TestAdapter(TestRepo repository, ObjectMapper mapper, Function<TestDocument, TestEntity> toEntityFn) {
            super(repository, mapper, toEntityFn);
        }
    }

    @BeforeEach
    void setUp() {
        repository = mock(TestRepo.class);
        mapper = mock(ObjectMapper.class);
        adapter = new TestAdapter(repository, mapper, d -> new TestEntity(d.id, d.name));
    }

    @Test
    void save_maps_and_returns_entity() {
        TestEntity entity = new TestEntity("1", "e1");
        TestDocument doc = new TestDocument("1", "e1");

        when(mapper.map(entity, TestDocument.class)).thenReturn(doc);
        when(repository.save(doc)).thenReturn(Mono.just(doc));

        StepVerifier.create(adapter.save(entity))
                .expectNextMatches(e -> e != null && e.id.equals("1") && e.name.equals("e1"))
                .verifyComplete();

        ArgumentCaptor<TestDocument> captor = ArgumentCaptor.forClass(TestDocument.class);
        verify(repository).save(captor.capture());
    }

    @Test
    void saveAll_returns_all_entities() {
        TestEntity e1 = new TestEntity("1", "a");
        TestEntity e2 = new TestEntity("2", "b");
        TestDocument d1 = new TestDocument("1", "a");
        TestDocument d2 = new TestDocument("2", "b");

        when(mapper.map(e1, TestDocument.class)).thenReturn(d1);
        when(mapper.map(e2, TestDocument.class)).thenReturn(d2);
        when(repository.saveAll(any(Flux.class))).thenReturn(Flux.just(d1, d2));

        StepVerifier.create(adapter.saveAll(Flux.just(e1, e2)))
                .expectNextMatches(x -> x.id.equals("1"))
                .expectNextMatches(x -> x.id.equals("2"))
                .verifyComplete();
    }

    @Test
    void findById_returns_entity_when_present() {
        TestDocument doc = new TestDocument("x", "n");
        when(repository.findById("x")).thenReturn(Mono.just(doc));

        StepVerifier.create(adapter.findById("x"))
                .expectNextMatches(e -> e.id.equals("x") && e.name.equals("n"))
                .verifyComplete();
    }

    @Test
    void findById_empty_when_missing() {
        when(repository.findById("nope")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById("nope"))
                .verifyComplete();
    }

    @Test
    void findByExample_uses_example_and_returns_entities() {
        TestEntity example = new TestEntity(null, "nm");
        TestDocument doc = new TestDocument("a", "nm");
        when(mapper.map(example, TestDocument.class)).thenReturn(doc);
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(doc));

        StepVerifier.create(adapter.findByExample(example))
                .expectNextMatches(e -> e.name.equals("nm"))
                .verifyComplete();
    }

    @Test
    void deleteById_delegates_to_repository() {
        when(repository.deleteById("del")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteById("del"))
                .verifyComplete();
    }

    @Test
    void findAll_returns_entities() {
        TestDocument d1 = new TestDocument("1", "a");
        TestDocument d2 = new TestDocument("2", "b");
        when(repository.findAll()).thenReturn(Flux.just(d1, d2));

        StepVerifier.create(adapter.findAll())
                .expectNextCount(2)
                .verifyComplete();
    }

}
