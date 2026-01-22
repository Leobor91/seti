package com.seti.franchise.usecase.product;

import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.excepcion.NotFoundException;
import com.seti.franchise.model.product.Product;
import com.seti.franchise.model.product.gateway.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Log
public class UpdateProductNameUseCase {

    private final ProductRepository productRepository;

    public Mono<Product> execute(String productId, String newName) {
        log.info("Starting update process for product ID: " + productId + " with new name: " + newName);
        return validate(newName, productId)
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("The 'name' and 'id' fields are required and must not be blank.")))
                .flatMap(valid -> productRepository.findByName(newName))
                .flatMap(existingProduct -> existingProduct.getId().equalsIgnoreCase(productId)
                        ? Mono.error(new IllegalArgumentException("The new name already belongs to the current product."))
                        : Mono.error(new DuplicateValueException("A product with the name '" + newName + "' already exists.")))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Name available for update");
                    return Mono.just(Boolean.TRUE);
                }))
                .flatMap(value -> productRepository.findById(productId))
                .switchIfEmpty(Mono.error( new NotFoundException("Product not found with ID: " + productId)))
                .flatMap(product ->  productRepository.updateName(productId, newName))
                .doOnSuccess(updatedProduct -> log.info("Updated product: " + updatedProduct));

    }

    private Mono<Boolean> validate(String productName, String productId) {
        boolean isValid = Objects.nonNull(productName) && !productName.isBlank() &&
                Objects.nonNull(productId) && !productId.isBlank();
        log.info("Validation result  '" + isValid);
        return Mono.just(isValid);
    }


}
