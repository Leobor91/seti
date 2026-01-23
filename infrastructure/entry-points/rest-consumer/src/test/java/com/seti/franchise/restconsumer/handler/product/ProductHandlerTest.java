package com.seti.franchise.restconsumer.handler.product;


import com.seti.franchise.model.product.Product;
import com.seti.franchise.model.product.TopStockProduct;
import com.seti.franchise.usecase.product.CreateProductUseCase;
import com.seti.franchise.usecase.product.DeleteProductUseCase;
import com.seti.franchise.usecase.product.GetTopStockByFranchiseUseCase;
import com.seti.franchise.usecase.product.UpdateProductNameUseCase;
import com.seti.franchise.usecase.product.UpdateProductStockUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ProductHandler.class)
@Import(TestConfig.class)
class ProductHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    CreateProductUseCase createProductUseCase;

    @MockitoBean
    UpdateProductNameUseCase updateProductNameUseCase;

    @MockitoBean
    UpdateProductStockUseCase updateProductStockUseCase;

    @MockitoBean
    DeleteProductUseCase deleteProductUseCase;

    @MockitoBean
    GetTopStockByFranchiseUseCase getTopStockByFranchiseUseCase;

    @Test
    void createProduct_returnsCreated() {
        Product product = Product.builder().id("p1").name("ProdA").branchId("b1").stock(5L).build();
        when(createProductUseCase.execute(eq("b1"), eq("ProdA"), eq(5L))).thenReturn(Mono.just(product));

        Map<String,Object> request = Map.of(
                "name","ProdA",
                "branchId","b1",
                "stock",5
        );

        webTestClient.post().uri("/api/v1/products")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.status").isEqualTo(201)
                .jsonPath("$.message").isEqualTo("Product created successfully")
                .jsonPath("$.data.name").isEqualTo("ProdA");
    }

    @Test
    void updateNameProduct_returnsOk() {
        Product product = Product.builder().id("p2").name("NewName").branchId("b2").stock(3L).build();
        when(updateProductNameUseCase.execute(eq("p2"), eq("NewName"), eq("b2"))).thenReturn(Mono.just(product));

        Map<String,Object> request = Map.of(
                "id","p2",
                "name","NewName",
                "branchId","b2"
        );

        webTestClient.put().uri("/api/v1/products/update-name/{id}", "p2")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200)
                .jsonPath("$.message").isEqualTo("Product updated successfully")
                .jsonPath("$.data.name").isEqualTo("NewName");
    }

    @Test
    void updateStockProduct_returnsOk() {
        Product product = Product.builder().id("p3").name("Stocked").branchId("b3").stock(10L).build();
        when(updateProductStockUseCase.execute(eq("p3"), eq(10L))).thenReturn(Mono.just(product));

        Map<String,Object> request = Map.of(
                "id","p3",
                "stock",10
        );

        webTestClient.put().uri("/api/v1/products/update-stock/{id}", "p3")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200)
                .jsonPath("$.message").isEqualTo("Stock Product updated successfully")
                .jsonPath("$.data.stock").isEqualTo(10);
    }

    @Test
    void deleteProduct_returnsOk() {
        Product product = Product.builder().id("p4").name("Deleted").branchId("b4").stock(0L).build();
        when(deleteProductUseCase.execute(eq("p4"))).thenReturn(Mono.just(product));

        webTestClient.delete().uri("/api/v1/products/{id}", "p4")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200)
                .jsonPath("$.message").isEqualTo("Product deleted successfully")
                .jsonPath("$.data.id").isEqualTo("p4")
                .jsonPath("$.data.name").isEqualTo("Deleted")
                .jsonPath("$.data.branchId").isEqualTo("b4")
                .jsonPath("$.data.stock").isEqualTo(0);
    }

    @Test
    void getTopStockProducts_returnsList() {
        TopStockProduct t = TopStockProduct.builder().branchId("b1").branchName("Branch 1").productName("P").stock(7).build();
        when(getTopStockByFranchiseUseCase.execute(eq("f1"))).thenReturn(Flux.just(t));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/api/v1/products/top-stock-products").queryParam("franchiseId","f1").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200)
                .jsonPath("$.message").isEqualTo("Top stock products retrieved successfully")
                .jsonPath("$.data[0].productName").isEqualTo("P");
    }

}
