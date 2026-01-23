package com.seti.franchise.usecase.product;

import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.excepcion.NotFoundException;
import com.seti.franchise.model.product.Product;
import com.seti.franchise.model.product.gateway.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class UpdateProductNameUseCaseTest {

    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    UpdateProductNameUseCase useCase = new UpdateProductNameUseCase(productRepository);

    @Test
    @DisplayName("updates when name available")
    void update_success() {
        String id = UUID.randomUUID().toString();
        Product existing = Product.builder().id(id).name("Old").branchId("b1").stock(1L).build();
        when(productRepository.findByNameAndBranchId(eq("New"), eq("b1"))).thenReturn(Mono.empty());
        when(productRepository.findById(eq(id))).thenReturn(Mono.just(existing));
        when(productRepository.updateName(eq(id), eq("New"))).thenReturn(Mono.just(existing.toBuilder().name("New").build()));

        StepVerifier.create(useCase.execute(id, "New", "b1"))
                .expectNextMatches(p -> p.getName().equals("New") && p.getId().equals(id))
                .verifyComplete();
    }

    @Test
    @DisplayName("fails when another product already has the name")
    void update_duplicateOther() {
        String id = UUID.randomUUID().toString();
        Product other = Product.builder().id(UUID.randomUUID().toString()).name("New").branchId("b1").stock(2L).build();
        when(productRepository.findByNameAndBranchId(eq("New"), eq("b1"))).thenReturn(Mono.just(other));

        StepVerifier.create(useCase.execute(id, "New", "b1"))
                .expectError(DuplicateValueException.class)
                .verify();
    }

    @Test
    @DisplayName("fails when name belongs to same product (treated as error)")
    void update_nameBelongsToSame() {
        String id = UUID.randomUUID().toString();
        Product same = Product.builder().id(id).name("New").branchId("b1").stock(2L).build();
        when(productRepository.findByNameAndBranchId(eq("New"), eq("b1"))).thenReturn(Mono.just(same));

        StepVerifier.create(useCase.execute(id, "New", "b1"))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    @DisplayName("fails when product id not found")
    void update_notFound() {
        String id = UUID.randomUUID().toString();
        when(productRepository.findByNameAndBranchId(eq("New"), eq("b1"))).thenReturn(Mono.empty());
        when(productRepository.findById(eq(id))).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(id, "New", "b1"))
                .expectError(NotFoundException.class)
                .verify();
    }

}
