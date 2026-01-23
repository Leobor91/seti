package com.seti.franchise.usecase.product;

import com.seti.franchise.model.branch.Branch;
import com.seti.franchise.model.franchise.Franchise;
import com.seti.franchise.model.product.Product;
import com.seti.franchise.model.product.TopStockProduct;
import com.seti.franchise.model.product.gateway.ProductRepository;
import com.seti.franchise.model.branch.gateway.BranchRepository;
import com.seti.franchise.model.franchise.gateway.FranchiseRepository;
import com.seti.franchise.model.excepcion.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetTopStockByFranchiseUseCaseTest {

    private ProductRepository productRepository;
    private BranchRepository branchRepository;
    private FranchiseRepository franchiseRepository;
    private GetTopStockByFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        branchRepository = mock(BranchRepository.class);
        franchiseRepository = mock(FranchiseRepository.class);
        useCase = new GetTopStockByFranchiseUseCase(productRepository, branchRepository, franchiseRepository);
    }

    @Test
    void execute_returns_top_stock_products_for_franchise() {
        String franchiseId = "f1";
        Franchise franchise = Franchise.builder().id(franchiseId).name("F").build();
        Branch b1 = Branch.builder().id("b1").franchiseId(franchiseId).name("Branch1").build();
        Branch b2 = Branch.builder().id("b2").franchiseId(franchiseId).name("Branch2").build();

        Product p1 = Product.builder().id("p1").branchId("b1").name("Prod1").stock(10L).build();
        Product p2 = Product.builder().id("p2").branchId("b2").name("Prod2").stock(5L).build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepository.findByFranchiseId(franchiseId)).thenReturn(Flux.just(b1, b2));
        when(productRepository.findFirstByBranchIdOrderByStockDesc("b1")).thenReturn(Mono.just(p1));
        when(productRepository.findFirstByBranchIdOrderByStockDesc("b2")).thenReturn(Mono.just(p2));

        StepVerifier.create(useCase.execute(franchiseId))
                .expectNextMatches(ts -> ts.getBranchId().equals("b1") && ts.getProductName().equals("Prod1") && ts.getStock() == 10)
                .expectNextMatches(ts -> ts.getBranchId().equals("b2") && ts.getProductName().equals("Prod2") && ts.getStock() == 5)
                .verifyComplete();
    }

    @Test
    void execute_throws_not_found_when_franchise_missing() {
        String franchiseId = "missing";
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(franchiseId))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().contains(franchiseId))
                .verify();
    }

}
