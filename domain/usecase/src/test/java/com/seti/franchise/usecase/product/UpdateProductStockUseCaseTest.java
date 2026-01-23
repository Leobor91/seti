package com.seti.franchise.usecase.product;

import com.seti.franchise.model.excepcion.NotFoundException;
import com.seti.franchise.model.product.Product;
import com.seti.franchise.model.product.gateway.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UpdateProductStockUseCaseTest {

    private ProductRepository productRepository;
    private UpdateProductStockUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        useCase = new UpdateProductStockUseCase(productRepository);
    }

    @Test
    void execute_updates_stock_when_valid() {
        String id = "p1";
        Long newStock = 5L;
        Product orig = Product.builder().id(id).branchId("b").name("N").stock(1L).build();
        Product updated = Product.builder().id(id).branchId("b").name("N").stock(newStock).build();

        when(productRepository.findById(id)).thenReturn(Mono.just(orig));
        when(productRepository.updateStock(id, newStock)).thenReturn(Mono.just(updated));

        StepVerifier.create(useCase.execute(id, newStock))
                .expectNextMatches(p -> p.getStock().equals(newStock) && p.getId().equals(id))
                .verifyComplete();
    }

    @Test
    void execute_throws_not_found_when_product_missing() {
        when(productRepository.findById("nope")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("nope", 2L))
                .expectErrorMatches(err -> err instanceof NotFoundException)
                .verify();
    }

    @Test
    void execute_fails_for_invalid_inputs() {
        StepVerifier.create(useCase.execute("", null))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException)
                .verify();
    }

}
