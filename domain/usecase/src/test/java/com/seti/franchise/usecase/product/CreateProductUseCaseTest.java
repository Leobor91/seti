package com.seti.franchise.usecase.product;

import com.seti.franchise.model.branch.Branch;
import com.seti.franchise.model.branch.gateway.BranchRepository;
import com.seti.franchise.model.excepcion.NotFoundException;
import com.seti.franchise.model.product.Product;
import com.seti.franchise.model.product.gateway.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CreateProductUseCaseTest {

    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    BranchRepository branchRepository = Mockito.mock(BranchRepository.class);
    CreateProductUseCase useCase = new CreateProductUseCase(productRepository, branchRepository);

    @Test
    @DisplayName("creates product when branch exists and name not used")
    void create_success() {
        String branchId = UUID.randomUUID().toString();
        Branch branch = Branch.builder().id(branchId).name("branch").franchiseId("f1").build();
        when(branchRepository.findById(eq(branchId))).thenReturn(Mono.just(branch));
        when(productRepository.findByNameAndBranchId(eq("P"), eq(branchId))).thenReturn(Mono.empty());
        when(productRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute(branchId, "P", 2L))
                .expectNextMatches(p -> p.getName().equals("P") && p.getBranchId().equals(branchId) && p.getStock().equals(2L))
                .verifyComplete();
    }

    @Test
    @DisplayName("fails when branch not found")
    void create_branchNotFound() {
        String branchId = "b-not";
        when(branchRepository.findById(eq(branchId))).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(branchId, "P", 1L))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("fails when product name already exists")
    void create_duplicateName() {
        String branchId = UUID.randomUUID().toString();
        Branch branch = Branch.builder().id(branchId).name("branch").franchiseId("f1").build();
        Product existing = Product.builder().id(UUID.randomUUID().toString()).name("P").branchId(branchId).stock(1L).build();
        when(branchRepository.findById(eq(branchId))).thenReturn(Mono.just(branch));
        when(productRepository.findByNameAndBranchId(eq("P"), eq(branchId))).thenReturn(Mono.just(existing));

        StepVerifier.create(useCase.execute(branchId, "P", 1L))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    @DisplayName("fails when invalid input")
    void create_invalidInput() {
        StepVerifier.create(useCase.execute("", null, null))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
