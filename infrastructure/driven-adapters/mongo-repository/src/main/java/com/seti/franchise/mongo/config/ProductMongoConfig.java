package com.seti.franchise.mongo.config;

import com.seti.franchise.model.product.Product;
import com.seti.franchise.mongo.product.ProductDocument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ProductMongoConfig {

    @Bean
    public Function<ProductDocument, Product> productDocumentToProduct(){
        return doc -> Product.builder()
                .id(doc.getId())
                .name(doc.getName())
                .branchId(doc.getBranchId())
                .stock(doc.getStock())
                .build();




    }
}
