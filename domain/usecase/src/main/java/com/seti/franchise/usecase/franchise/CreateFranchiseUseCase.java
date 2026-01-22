package com.seti.franchise.usecase.franchise;

import com.seti.franchise.model.excepcion.FranchiseAlreadyExistsException;
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
                .flatMap(franchiseRepository::findByName)
                .flatMap(existing -> Mono.<Franchise>error(
                        new FranchiseAlreadyExistsException("Franchise with name '" + franchiseName + "' already exists.")
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

    private Mono<String> validate(String franchiseName) {
        return Objects.nonNull(franchiseName) && !franchiseName.isBlank() ?
                Mono.just(franchiseName) :
                Mono.error(new IllegalArgumentException("Franchise name cannot be null or empty"));
    }
}
