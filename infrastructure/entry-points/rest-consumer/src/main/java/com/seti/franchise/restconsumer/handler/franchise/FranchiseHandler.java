package com.seti.franchise.restconsumer.handler.franchise;


import com.seti.franchise.restconsumer.dto.request.FranchiseRequest;
import com.seti.franchise.restconsumer.dto.response.ApiResponseDto;
import com.seti.franchise.usecase.franchise.CreateFranchiseUseCase;
import com.seti.franchise.usecase.franchise.UpdateFranchiseNameUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;




@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/franchises")
@Validated
@Slf4j
public class FranchiseHandler {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    @PostMapping
    public Mono<ResponseEntity<ApiResponseDto>> createFranchise(@Valid @RequestBody FranchiseRequest requestBody) {
        log.info("Request body = {}", requestBody);
        return createFranchiseUseCase.execute(requestBody.getName())
                .doOnNext(franchise -> log.info("Franchise created: {}", franchise))
                .map(franchise ->  ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(ApiResponseDto.builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Franchise created successfully")
                                .data(franchise)
                                .build()));
    }

    @PutMapping(path = "/{id}")
    public Mono<ResponseEntity<ApiResponseDto>> updateFranchise(@PathVariable("id")  String id, @Valid @RequestBody FranchiseRequest requestBody) {
        log.info("Request to update franchise: {}", requestBody);
        return updateFranchiseNameUseCase.execute(id, requestBody.getName())
                .doOnNext(franchise -> log.info("Franchise updated: {}", franchise))
                .map(franchise ->  ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ApiResponseDto.builder()
                                .status(HttpStatus.OK.value())
                                .message("Franchise updated successfully")
                                .data(franchise)
                                .build()));
    }
}
