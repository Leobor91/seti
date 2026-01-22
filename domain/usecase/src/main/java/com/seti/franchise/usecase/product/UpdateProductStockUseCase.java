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
public class UpdateProductStockUseCase {

    private final ProductRepository productRepository;

    public Mono<Product> execute(String productId, Long newStock) {
        log.info("Starting stock update for product ID: " + productId + " with new stock: " + newStock);
        return validate(productId, newStock)
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product ID and new stock must be provided and valid.")))
                .flatMap(valid -> validate(newStock))
                .filter(isValidStock -> !isValidStock)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Stock must be a non-negative value.")))
                .flatMap(validStock -> productRepository.findById(productId))
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found with ID:" + productId)))
                .flatMap(product -> productRepository.updateStock(productId, newStock))
                .doOnSuccess(updatedProduct -> log.info("Stock updated product: " + updatedProduct));
    }

    private Mono<Boolean> validate(String productId, Long newStock) {
        boolean isValid = Objects.nonNull(newStock) && !newStock.toString().isBlank() &&
                Objects.nonNull(productId) && !productId.isBlank();
        log.info("Validation result: " + isValid);
        return Mono.just(isValid);

    }

    private Mono<Boolean> validate(Long newStock) {
        boolean isvalid = Objects.nonNull(newStock) && !newStock.toString().isBlank() && !(newStock >= 0);
        log.info("Stock validation result: " + isvalid);
        return Mono.just(isvalid);
    }
}