package com.seti.franchise.usecase.franchise;

import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.excepcion.NotFoundException;
import com.seti.franchise.model.franchise.Franchise;
import com.seti.franchise.model.franchise.gateway.FranchiseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class UpdateFranchiseNameUseCaseTest {

    FranchiseRepository franchiseRepository = Mockito.mock(FranchiseRepository.class);
    UpdateFranchiseNameUseCase useCase = new UpdateFranchiseNameUseCase(franchiseRepository);

    @Test
    @DisplayName("updates franchise name when available")
    void update_whenNameAvailable() {
        String id = UUID.randomUUID().toString();
        Franchise existing = Franchise.builder().id(id).name("Old").build();
        when(franchiseRepository.findByName(eq("New"))).thenReturn(Mono.empty());
        when(franchiseRepository.findById(eq(id))).thenReturn(Mono.just(existing));
        when(franchiseRepository.updateName(eq(id), eq("New"))).thenReturn(Mono.just(existing.toBuilder().name("New").build()));

        StepVerifier.create(useCase.execute(id, "New"))
                .expectNextMatches(f -> f.getName().equals("New") && f.getId().equals(id))
                .verifyComplete();
    }

    @Test
    @DisplayName("throws DuplicateValueException when name exists")
    void update_whenNameExists() {
        String id = UUID.randomUUID().toString();
        Franchise other = Franchise.builder().id(UUID.randomUUID().toString()).name("New").build();
        when(franchiseRepository.findByName(eq("New"))).thenReturn(Mono.just(other));

        StepVerifier.create(useCase.execute(id, "New"))
                .expectError(DuplicateValueException.class)
                .verify();
    }

    @Test
    @DisplayName("throws NotFoundException when franchise id not found")
    void update_whenIdNotFound() {
        String id = UUID.randomUUID().toString();
        when(franchiseRepository.findByName(eq("New"))).thenReturn(Mono.empty());
        when(franchiseRepository.findById(eq(id))).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(id, "New"))
                .expectError(NotFoundException.class)
                .verify();
    }

}
