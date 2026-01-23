package com.seti.franchise.restconsumer.handler.product;

import com.seti.franchise.restconsumer.dto.request.ProductRequest;
import com.seti.franchise.restconsumer.dto.response.ApiResponseDto;
import com.seti.franchise.usecase.product.CreateProductUseCase;
import com.seti.franchise.usecase.product.DeleteProductUseCase;
import com.seti.franchise.usecase.product.GetTopStockByFranchiseUseCase;
import com.seti.franchise.usecase.product.UpdateProductNameUseCase;
import com.seti.franchise.usecase.product.UpdateProductStockUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
@Validated
@Slf4j
public class ProductHandler {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetTopStockByFranchiseUseCase getTopStockByFranchiseUseCase;

    @PostMapping
    public Mono<ResponseEntity<ApiResponseDto>> createProduct(@Valid @RequestBody ProductRequest requestBody) {
        log.info("Request to create product received: {}", requestBody);
        return createProductUseCase.execute(requestBody.getBranchId(), requestBody.getName(), requestBody.getStock())
                .doOnNext(product -> log.info("Product created: {}", product))
                .map(product -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(ApiResponseDto.builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Product created successfully")
                                .data(product)
                                .build()));
    }

    @PutMapping(path = "/update-name/{id}")
    public Mono<ResponseEntity<ApiResponseDto>> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest requestBody) {
        log.info("Request to update product received: {}", requestBody);
        return updateProductNameUseCase.execute(id, requestBody.getName(), requestBody.getBranchId())
                .doOnNext(product -> log.info("Product updated: {}", product))
                .map(product -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ApiResponseDto.builder()
                                .status(HttpStatus.OK.value())
                                .message("Product updated successfully")
                                .data(product)
                                .build()));
    }

    @PutMapping(path = "/update-stock/{id}")
    public Mono<ResponseEntity<ApiResponseDto>> updateStockProduct(@Valid @PathVariable String id, @RequestBody ProductRequest requestBody){
        log.info("Request to update product stock received: {}", requestBody);
        return updateProductStockUseCase.execute(id, requestBody.getStock())
                .doOnNext(product -> log.info("Product stock updated: {}", product))
                .map(product -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ApiResponseDto.builder()
                                .status(HttpStatus.OK.value())
                                .message("Stock Product updated successfully")
                                .data(product)
                                .build()));
    }

    @DeleteMapping(path = "/{id}")
    public Mono<ResponseEntity<ApiResponseDto>> deleteProduct(@PathVariable String id) {
        log.info("Request to delete product received: {}", id);
        return deleteProductUseCase.execute(id)
                .doOnSuccess(productDelete -> log.info("Product deleted with id: {}", id))
                .map(productDelete -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ApiResponseDto.builder()
                                .status(HttpStatus.OK.value())
                                .message("Product deleted successfully")
                                .data(productDelete)
                                .build()));
    }

    @GetMapping(path = "/top-stock-products")
    public Mono<ResponseEntity<ApiResponseDto>> getTopStockProducts(@RequestParam("franchiseId") String franchiseId) {
        log.info("Request to get top stock products received");
        return getTopStockByFranchiseUseCase.execute(franchiseId)
                .collectList()
                .doOnNext(products -> log.info("Top stock products retrieved: {}", products))
                .map(products -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ApiResponseDto.builder()
                                .status(HttpStatus.OK.value())
                                .message("Top stock products retrieved successfully")
                                .data(products)
                                .build()));
    }


}
