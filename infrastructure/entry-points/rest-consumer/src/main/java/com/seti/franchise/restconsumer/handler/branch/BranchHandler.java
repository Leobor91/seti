package com.seti.franchise.restconsumer.handler.branch;

import com.seti.franchise.restconsumer.dto.request.BranchRequest;
import com.seti.franchise.restconsumer.dto.response.ApiResponseDto;
import com.seti.franchise.usecase.branch.CreateBranchUseCase;
import com.seti.franchise.usecase.branch.UpdateBranchNameUseCase;
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
@RequestMapping("/api/v1/branches")
@Validated
@Slf4j
public class BranchHandler {

    private final CreateBranchUseCase createBranchUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;

    @PostMapping
    public Mono<ResponseEntity<ApiResponseDto>> createBranch(@Valid @RequestBody BranchRequest requestBody){
        log.info("Request to create branch: {}", requestBody);
        return createBranchUseCase.execute(requestBody.getFranchiseId(), requestBody.getName())
                .doOnNext(branch -> log.info("Branch created: {}", branch))
                .map(branch -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponseDto.builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Branch created successfully")
                                .data(branch)
                                .build()));

    }

    @PutMapping(path = "/{id}")
    public Mono<ResponseEntity<ApiResponseDto>> updateBranch(@PathVariable String id, @Valid @RequestBody BranchRequest requestBody){
        log.info("Request to update branch: {}", requestBody);
        return updateBranchNameUseCase.execute(id, requestBody.getName())
                .doOnNext(branch -> log.info("Branch updated: {}", branch))
                .map(branch -> ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponseDto.builder()
                                .status(HttpStatus.OK.value())
                                .message("Branch updated successfully")
                                .data(branch)
                                .build()));
    }
}
