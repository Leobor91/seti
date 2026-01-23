package com.seti.franchise.usecase.product;

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

class DeleteProductUseCaseTest {

    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    DeleteProductUseCase useCase = new DeleteProductUseCase(productRepository);

    @Test
    @DisplayName("deletes existing product")
    void delete_success() {
        String id = UUID.randomUUID().toString();
        Product p = Product.builder().id(id).name("X").branchId("b").stock(1L).build();
        when(productRepository.findById(eq(id))).thenReturn(Mono.just(p));
        when(productRepository.deleteById(eq(id))).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(id))
                .expectNextMatches(prod -> prod.getId().equals(id))
                .verifyComplete();
    }

    @Test
    @DisplayName("fails when product not found")
    void delete_notFound() {
        String id = UUID.randomUUID().toString();
        when(productRepository.findById(eq(id))).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(id))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("fails when invalid id")
    void delete_invalidId() {
        StepVerifier.create(useCase.execute(""))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

}
