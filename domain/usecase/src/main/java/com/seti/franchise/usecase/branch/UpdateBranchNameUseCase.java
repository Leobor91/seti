package com.seti.franchise.usecase.branch;

import com.seti.franchise.model.branch.Branch;
import com.seti.franchise.model.branch.gateway.BranchRepository;
import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.excepcion.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Log
public class UpdateBranchNameUseCase {

    private final BranchRepository branchRepository;

    public Mono<Branch> execute(String branchId, String newName) {
        log.info("Starting update process for branch: " + branchId + " with new name: " + newName);
        return validate(branchId, newName)
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("The 'name' (Branch Name) and 'id;' fields are required and must not")))
                .flatMap(isValid -> branchRepository.findByName(newName))
                .flatMap(existingBranch -> existingBranch.getId().equalsIgnoreCase(branchId)
                        ? Mono.error(new IllegalArgumentException("The new name already belongs to the current branch "))
                        : Mono.error(new DuplicateValueException("A branch with the name '" + newName + "' already exists.")))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Name available for update");
                    return Mono.just(Boolean.TRUE);
                }))
                .flatMap(value -> branchRepository.findById(branchId))
                .switchIfEmpty(Mono.error(new NotFoundException("Branch not found with ID: " + branchId)))
                .flatMap(branch -> branchRepository.updateName(branchId, newName))
                .doOnSuccess(updatedBranch -> log.info("Updated branch: " + updatedBranch));
    }

    private Mono<Boolean> validate(String branchId, String newName) {
        boolean isValid = Objects.nonNull(branchId) && !branchId.isBlank() &&
                Objects.nonNull(newName) && !newName.isBlank();
        log.info("Validation result: " + isValid);
        return Mono.just(isValid);
    }
}
