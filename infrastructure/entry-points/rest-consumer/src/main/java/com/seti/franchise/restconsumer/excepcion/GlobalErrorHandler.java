package com.seti.franchise.restconsumer.excepcion;

import com.seti.franchise.model.excepcion.FranchiseAlreadyExistsException;
import com.seti.franchise.restconsumer.dto.response.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(FranchiseAlreadyExistsException.class)
    public Mono<ResponseEntity<ApiResponseDto>> handleDuplicateKey(FranchiseAlreadyExistsException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponseDto.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .message(ex.getMessage())
                        .data(null)
                        .build()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ApiResponseDto>> handleBadRequest(IllegalArgumentException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .data(null)
                        .build()));
    }
}

