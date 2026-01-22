package com.seti.franchise.usecase.franchise;

import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.franchise.Franchise;
import com.seti.franchise.model.franchise.gateway.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Log
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String franchiseId, String newName) {
        return validate(newName, franchiseId)
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Franchise name and ID must be provided and non-blank.")
                ))
                .flatMap(isValid -> franchiseRepository.findByName(newName))
                .flatMap(existingFranchise -> existingFranchise.getId().equalsIgnoreCase(franchiseId)
                        ?  Mono.error(new IllegalArgumentException("The new name already belongs to the current franchise."))
                        :  Mono.error(new DuplicateValueException("Franchise with name '" + newName + "' already exists."))

                )
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Name available for update");
                    return Mono.just(Boolean.TRUE);
                }))
                .flatMap(value -> franchiseRepository.findById(franchiseId))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found with ID: " + franchiseId)))
                .flatMap(franchise -> franchiseRepository.updateName(franchiseId, newName))
                .doOnSuccess(updatedFranchise ->
                        log.info("Franchise name updated successfully to: " + updatedFranchise.getName())
                );


    }

    private Mono<Boolean> validate(String franchiseName, String franchiseId) {
        boolean isValid = Objects.nonNull(franchiseName) && !franchiseName.isBlank() &&
                Objects.nonNull(franchiseId) && !franchiseId.isBlank();
        return Mono.just(isValid);
    }
}
