package com.seti.franchise.config;

import com.seti.franchise.model.product.gateway.ProductRepository;
import com.seti.franchise.usecase.product.UpdateProductStockUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "com.seti.franchise.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

        @Bean
        public UpdateProductStockUseCase updateProductStockUseCase(ProductRepository productRepository) {
                return new UpdateProductStockUseCase(productRepository);
        }
}
