package com.seti.franchise.usecase.product;

import com.seti.franchise.model.excepcion.NotFoundException;
import com.seti.franchise.model.product.Product;
import com.seti.franchise.model.product.gateway.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Log
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    private Mono<Void> execute(String productId){
        log.info("Deleting product '" + productId + "'");
        return validate(productId)
                .filter(isvalid -> isvalid)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product ID must not be null or empty.")))
                .flatMap(isValid -> productRepository.findById(productId))
                .switchIfEmpty(Mono.error( new NotFoundException("Product not found with ID: " + productId)))
                .flatMap(product -> productRepository.deleteById(product.getId()))
                .doOnSuccess(result -> log.info("Deleted product '" + productId + "'"));
    }

    private Mono<Boolean> validate(String productId) {
        boolean isValid = Objects.nonNull(productId) && !productId.isBlank();
        log.info("Validation result  '" + isValid);
        return Mono.just(isValid);
    }
}
