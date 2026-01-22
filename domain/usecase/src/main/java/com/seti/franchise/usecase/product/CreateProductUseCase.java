package com.seti.franchise.usecase.product;

import com.seti.franchise.model.branch.gateway.BranchRepository;
import com.seti.franchise.model.excepcion.NotFoundException;
import com.seti.franchise.model.product.Product;
import com.seti.franchise.model.product.gateway.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Log
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public Mono<Product> execute(String branchId, String productName, Long stock) {
        log.info("Starting product creation process for product: " + productName + " in branch: " + branchId);
        return validate(productName, branchId)
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("The 'name' and 'brachId' fields are required and must not be blank.")
                ))
                .flatMap(isValid -> validate(stock))
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Stock cannot be null or empty, and must be a value greater than or equal to zero.")))
                .flatMap(valid -> branchRepository.findById(branchId))
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Branch not found with ID: " + branchId)
                ))
                .flatMap(branch -> productRepository.findByName(productName))
                .flatMap(existingProduct -> Mono.<Product>error(
                        new IllegalArgumentException("Product with name '" + productName + "' already exists.")
                ))
                .switchIfEmpty(Mono.defer(() -> productRepository.save(
                        Product.builder()
                                .id(UUID.randomUUID().toString())
                                .name(productName)
                                .branchId(branchId)
                                .build()
                )));

    }

    private Mono<Boolean> validate(String productName, String branchId) {
        boolean isValid = Objects.nonNull(productName) && !productName.isBlank() &&
                Objects.nonNull(branchId) && !branchId.isBlank();
        log.info("Validation result  '" + isValid);
        return Mono.just(isValid);
    }

    private Mono<Boolean> validate(Long newStock) {
        boolean isvalid = Objects.nonNull(newStock) && !newStock.toString().isBlank() && !(newStock >= 0);
        log.info("Stock validation result: "  + isvalid);
        return Mono.just(isvalid);
    }
}
