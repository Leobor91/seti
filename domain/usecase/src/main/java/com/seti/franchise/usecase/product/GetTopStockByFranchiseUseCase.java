package com.seti.franchise.usecase.product;

import com.seti.franchise.model.branch.Branch;
import com.seti.franchise.model.branch.gateway.BranchRepository;
import com.seti.franchise.model.excepcion.NotFoundException;
import com.seti.franchise.model.franchise.gateway.FranchiseRepository;
import com.seti.franchise.model.product.Product;
import com.seti.franchise.model.product.TopStockProduct;
import com.seti.franchise.model.product.gateway.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
public class GetTopStockByFranchiseUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Flux<TopStockProduct> execute(String franchiseId) {
        log.info("Fetching top stock products for franchise: " + franchiseId);

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franchise not found: " + franchiseId)))
                .flatMapMany(franchise ->
                        branchRepository.findByFranchiseId(franchise.getId())
                                .flatMap(branch ->
                                        productRepository.findFirstByBranchIdOrderByStockDesc(branch.getId())
                                                .map(product -> buildResponse(branch, product))
                                )
                );
    }

    private TopStockProduct buildResponse(Branch branch, Product product) {
        return TopStockProduct.builder()
                .branchId(branch.getId())
                .branchName(branch.getName())
                .productName(product.getName())
                .stock(Math.toIntExact(product.getStock()))
                .build();
    }

}
