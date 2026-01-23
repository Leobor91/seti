package com.seti.franchise.restconsumer.handler.branch;

import com.seti.franchise.model.branch.Branch;
import com.seti.franchise.usecase.branch.CreateBranchUseCase;
import com.seti.franchise.usecase.branch.UpdateBranchNameUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = BranchHandler.class)
@Import(TestConfig.class)
class BranchHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    CreateBranchUseCase createBranchUseCase;

    @MockitoBean
    UpdateBranchNameUseCase updateBranchNameUseCase;

    @Test
    void createBranch_returnsCreated() {
        Branch b = Branch.builder().id("br1").franchiseId("f1").name("Branch One").build();
        when(createBranchUseCase.execute(eq("f1"), eq("Branch One"))).thenReturn(Mono.just(b));

        Map<String,Object> request = Map.of(
                "name","Branch One",
                "franchiseId","f1"
        );

        webTestClient.post().uri("/api/v1/branches")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.status").isEqualTo(201)
                .jsonPath("$.message").isEqualTo("Branch created successfully")
                .jsonPath("$.data.name").isEqualTo("Branch One");
    }

    @Test
    void updateBranch_returnsOk() {
        Branch b = Branch.builder().id("br2").franchiseId("f2").name("Updated").build();
        when(updateBranchNameUseCase.execute(eq("br2"), eq("Updated"))).thenReturn(Mono.just(b));

        Map<String,Object> request = Map.of(
                "id","br2",
                "name","Updated"
        );

        webTestClient.put().uri("/api/v1/branches/{id}", "br2")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200)
                .jsonPath("$.message").isEqualTo("Branch updated successfully")
                .jsonPath("$.data.name").isEqualTo("Updated");
    }

}
