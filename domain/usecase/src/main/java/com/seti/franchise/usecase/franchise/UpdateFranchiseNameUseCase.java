package com.seti.franchise.usecase.franchise;

import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.excepcion.NotFoundException;
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
        log.info("Starting update process for franchise: " + franchiseId + ", newName: " + newName);
        return validate(newName, franchiseId)
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Both 'name' and 'id' fields are required and must not be empty")
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
                .switchIfEmpty(Mono.error(new NotFoundException("Franchise not found with ID: " + franchiseId)))
                .flatMap(franchise -> franchiseRepository.updateName(franchiseId, newName))
                .doOnSuccess(updatedFranchise ->
                        log.info("Franchise name updated successfully to: " + updatedFranchise.getName())
                );


    }

    private Mono<Boolean> validate(String franchiseName, String franchiseId) {
        boolean isValid = Objects.nonNull(franchiseName) && !franchiseName.isBlank() &&
                Objects.nonNull(franchiseId) && !franchiseId.isBlank();
        log.info("Validation result: " + isValid);
        return Mono.just(isValid);
    }
}
