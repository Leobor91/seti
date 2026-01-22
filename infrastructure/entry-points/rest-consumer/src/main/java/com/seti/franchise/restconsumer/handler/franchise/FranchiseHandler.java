package com.seti.franchise.restconsumer.handler.franchise;


import com.seti.franchise.model.franchise.Franchise;
import com.seti.franchise.restconsumer.dto.request.FranchiseRequest;
import com.seti.franchise.restconsumer.dto.response.ApiResponseDto;
import com.seti.franchise.usecase.franchise.CreateFranchiseUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import reactor.core.publisher.Mono;


import java.net.URI;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/franchises")
@Validated
@Slf4j
public class FranchiseHandler {

    private final CreateFranchiseUseCase createFranchiseUseCase;

    @PostMapping(path = "/create")
    public Mono<ResponseEntity<ApiResponseDto>> create(@Valid @RequestBody FranchiseRequest requestBody) {
        log.info("Request body = {}", requestBody);
        return createFranchiseUseCase.execute(requestBody.getName())
                .doOnNext(franchise -> log.info("Franchise created: {}", franchise))
                .map(franchise ->  ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(ApiResponseDto.builder()
                                .status(201)
                                .message("Franchise created successfully")
                                .data(franchise)
                                .build()));
    }
}
