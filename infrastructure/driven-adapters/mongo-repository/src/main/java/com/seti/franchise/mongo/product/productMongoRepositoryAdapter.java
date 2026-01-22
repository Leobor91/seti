package com.seti.franchise.mongo.product;

import com.seti.franchise.model.product.Product;
import com.seti.franchise.model.product.gateway.ProductRepository;
import com.seti.franchise.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Repository
public class productMongoRepositoryAdapter extends AdapterOperations<Product, ProductDocument, String, ProductMongoDBRepository> implements ProductRepository {

    protected productMongoRepositoryAdapter(ProductMongoDBRepository repository, ObjectMapper mapper, Function<ProductDocument, Product> toEntityFn) {
        super(repository, mapper, toEntityFn);
    }

    @Override
    public Mono<Product> updateStock(String id, Long newStock) {
        return this.repository.findById(id)
                .flatMap(productDocument ->
                        this.repository.save(productDocument.toBuilder()
                                .stock(newStock)
                                .build()))
                .map(this::toEntity);
    }

    @Override
    public Mono<Product> updateName(String id, String newName) {
        return this.repository.findById(id)
                .flatMap(productDocument ->
                        this.repository.save(productDocument.toBuilder()
                                .name(newName)
                                .build()))
                .map(this::toEntity);
    }

    @Override
    public Mono<Product> findByName(String name) {
        return this.repository.findByName(name)
                .map(this::toEntity);
    }

    @Override
    public Mono<Product> save(Product product) {
        return this.repository.save(toData(product))
                .map(this::toEntity);
    }

    @Override
    protected ProductDocument toData(Product model) {
        return ProductDocument.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    @Override
    protected Product toEntity(ProductDocument document) {
        return Product.builder()
                .id(document.getId())
                .name(document.getName())
                .build();
    }
}
