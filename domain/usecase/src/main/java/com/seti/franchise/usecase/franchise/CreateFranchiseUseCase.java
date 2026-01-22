package com.seti.franchise.usecase.franchise;

import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.franchise.Franchise;
import com.seti.franchise.model.franchise.gateway.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Log
public class CreateFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String franchiseName) {
        return validate(franchiseName)
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Franchise name and ID must be provided and non-blank.")
                ))
                .flatMap(isValid -> franchiseRepository.findByName(franchiseName))
                .flatMap(existing -> Mono.<Franchise>error(
                        new DuplicateValueException("Franchise with name '" + franchiseName + "' already exists.")
                ))
                .switchIfEmpty(Mono.defer(() -> {
                    Franchise franchise = Franchise.builder()
                            .id(UUID.randomUUID().toString())
                            .name(franchiseName)
                            .build();
                    return franchiseRepository.save(franchise);
                }))
                .doOnSuccess(saved -> log.info("Franchise created with ID: " + saved.getName()));
    }

    private Mono<Boolean> validate(String franchiseName) {
        boolean isValid = Objects.nonNull(franchiseName) && !franchiseName.isBlank();
        return Mono.just(isValid);
    }
}
