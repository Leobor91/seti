package com.seti.franchise.usecase.branch;

import com.seti.franchise.model.branch.Branch;
import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.franchise.Franchise;
import com.seti.franchise.model.branch.gateway.BranchRepository;
import com.seti.franchise.model.franchise.gateway.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateBranchUseCaseTest {

    private BranchRepository branchRepository;
    private FranchiseRepository franchiseRepository;
    private CreateBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        branchRepository = mock(BranchRepository.class);
        franchiseRepository = mock(FranchiseRepository.class);
        when(branchRepository.findByNameAndFranchiseId(any(), any())).thenReturn(Mono.empty());
        useCase = new CreateBranchUseCase(branchRepository, franchiseRepository);
    }

    @Test
    void execute_fails_validation_for_blank_inputs() {
        StepVerifier.create(useCase.execute("", ""))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException)
                .verify();
    }

    @Test
    void execute_fails_when_franchise_not_found() {
        when(franchiseRepository.findById("f1")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("f1", "BranchX"))
                .expectErrorMatches(err -> err instanceof NoClassDefFoundError && err.getMessage().contains("Franchise not found"))
                .verify();
    }

    @Test
    void execute_fails_on_duplicate_branch() {
        when(franchiseRepository.findById("f1")).thenReturn(Mono.just(Franchise.builder().id("f1").name("F").build()));
        when(branchRepository.findByNameAndFranchiseId("B1", "f1")).thenReturn(Mono.just(Branch.builder().id("b1").name("B1").franchiseId("f1").build()));

        StepVerifier.create(useCase.execute("f1", "B1"))
                .expectErrorMatches(err -> err instanceof DuplicateValueException)
                .verify();
    }

    @Test
    void execute_saves_branch_when_ok() {
        when(franchiseRepository.findById("f1")).thenReturn(Mono.just(Franchise.builder().id("f1").name("F").build()));
        when(branchRepository.findByNameAndFranchiseId("B2", "f1")).thenReturn(Mono.empty());
        when(branchRepository.save(any(Branch.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("f1", "B2"))
                .expectNextMatches(b -> b.getName().equals("B2") && b.getFranchiseId().equals("f1"))
                .verifyComplete();
    }

}
