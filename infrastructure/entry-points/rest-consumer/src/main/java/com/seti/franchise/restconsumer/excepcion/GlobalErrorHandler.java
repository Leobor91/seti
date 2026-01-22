package com.seti.franchise.restconsumer.excepcion;

import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.excepcion.NotFoundException;
import com.seti.franchise.restconsumer.dto.response.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(DuplicateValueException.class)
    public Mono<ResponseEntity<ApiResponseDto>> handleDuplicateKey(DuplicateValueException ex) {
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

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ApiResponseDto>> handleDuplicateKey(NotFoundException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDto.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .data(null)
                        .build()));
    }
}

