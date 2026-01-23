package com.seti.franchise.restconsumer.handler.franchise;

import com.seti.franchise.model.franchise.Franchise;
import com.seti.franchise.usecase.franchise.CreateFranchiseUseCase;
import com.seti.franchise.usecase.franchise.UpdateFranchiseNameUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = FranchiseHandler.class)
@Import(TestConfig.class)
class FranchiseHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    CreateFranchiseUseCase createFranchiseUseCase;

    @MockitoBean
    UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    @Test
    void createFranchise_returnsCreated() {
        Franchise f = Franchise.builder().id("fr1").name("Franchise 1").build();
        when(createFranchiseUseCase.execute(eq("Franchise 1"))).thenReturn(Mono.just(f));

        Map<String,Object> request = Map.of(
                "name","Franchise 1"
        );

        webTestClient.post().uri("/api/v1/franchises/create")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.status").isEqualTo(201)
                .jsonPath("$.message").isEqualTo("Franchise created successfully")
                .jsonPath("$.data.name").isEqualTo("Franchise 1");
    }

    @Test
    void updateFranchise_returnsOk() {
        Franchise f = Franchise.builder().id("fr2").name("NewName").build();
        when(updateFranchiseNameUseCase.execute(eq("fr2"), eq("NewName"))).thenReturn(Mono.just(f));

        Map<String,Object> request = Map.of(
                "id","fr2",
                "name","NewName"
        );

        webTestClient.put().uri("/api/v1/franchises/update")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200)
                .jsonPath("$.message").isEqualTo("Franchise updated successfully")
                .jsonPath("$.data.name").isEqualTo("NewName");
    }

}
