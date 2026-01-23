package com.seti.franchise.usecase.franchise;

import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.franchise.Franchise;
import com.seti.franchise.model.franchise.gateway.FranchiseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CreateFranchiseUseCaseTest {

    FranchiseRepository franchiseRepository = Mockito.mock(FranchiseRepository.class);
    CreateFranchiseUseCase useCase = new CreateFranchiseUseCase(franchiseRepository);

    @Test
    @DisplayName("creates franchise when name not exists")
    void create_whenNameNotExists() {
        when(franchiseRepository.findByName(eq("X"))).thenReturn(Mono.empty());
        when(franchiseRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("X"))
                .expectNextMatches(f -> f.getName().equals("X") && f.getId() != null)
                .verifyComplete();
    }

    @Test
    @DisplayName("fails when franchise name already exists")
    void create_whenNameExists() {
        Franchise existing = Franchise.builder().id(UUID.randomUUID().toString()).name("X").build();
        when(franchiseRepository.findByName(eq("X"))).thenReturn(Mono.just(existing));

        StepVerifier.create(useCase.execute("X"))
                .expectError(DuplicateValueException.class)
                .verify();
    }

}
