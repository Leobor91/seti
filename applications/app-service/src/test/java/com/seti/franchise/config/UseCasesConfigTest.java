package com.seti.franchise.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }

        @Bean
        public com.seti.franchise.model.product.gateway.ProductRepository productRepository() {
            return new com.seti.franchise.model.product.gateway.ProductRepository() {
                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.product.Product> save(com.seti.franchise.model.product.Product product) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.product.Product> findById(String id) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.product.Product> updateStock(String id, Long newStock) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.product.Product> updateName(String id, String newName) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<Void> deleteById(String id) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.product.Product> findByName(String name) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.product.Product> findByNameAndBranchId(String name, String branchId) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.product.Product> findFirstByBranchIdOrderByStockDesc(String branchId) {
                    return reactor.core.publisher.Mono.empty();
                }
            };
        }

        @Bean
        public com.seti.franchise.model.branch.gateway.BranchRepository branchRepository() {
            return new com.seti.franchise.model.branch.gateway.BranchRepository() {
                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.branch.Branch> save(com.seti.franchise.model.branch.Branch branch) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.branch.Branch> findById(String id) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.branch.Branch> updateName(String id, String newName) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.branch.Branch> findByName(String name) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Flux<com.seti.franchise.model.branch.Branch> findByFranchiseId(String franchiseId) {
                    return reactor.core.publisher.Flux.empty();
                }
            };
        }

        @Bean
        public com.seti.franchise.model.franchise.gateway.FranchiseRepository franchiseRepository() {
            return new com.seti.franchise.model.franchise.gateway.FranchiseRepository() {
                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.franchise.Franchise> save(com.seti.franchise.model.franchise.Franchise franchise) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.franchise.Franchise> findById(String id) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.franchise.Franchise> updateName(String id, String newName) {
                    return reactor.core.publisher.Mono.empty();
                }

                @Override
                public reactor.core.publisher.Mono<com.seti.franchise.model.franchise.Franchise> findByName(String name) {
                    return reactor.core.publisher.Mono.empty();
                }
            };
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}