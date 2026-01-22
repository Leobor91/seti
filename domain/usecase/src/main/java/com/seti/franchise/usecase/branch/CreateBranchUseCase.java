package com.seti.franchise.usecase.branch;

import com.seti.franchise.model.branch.Branch;
import com.seti.franchise.model.branch.gateway.BranchRepository;
import com.seti.franchise.model.excepcion.DuplicateValueException;
import com.seti.franchise.model.franchise.gateway.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Log
public class CreateBranchUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Branch> execute(String franchiseId, String branchName) {
        log.info("Starting creation process for branch: " + branchName + " under franchise ID: " + franchiseId);
        return validate(branchName, franchiseId)
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("The 'name' (Branch Name) and 'franchiseId' fields are required and must not")
                ))
                .flatMap(valid -> franchiseRepository.findById(franchiseId))
                .switchIfEmpty(Mono.error(
                        new NoClassDefFoundError("Franchise not found with ID: " + franchiseId)
                ))
                .flatMap(franchise -> branchRepository.findByName(branchName)
                        .flatMap(existingBranch -> Mono.<Branch>error(
                                new DuplicateValueException("Branch with name '" + branchName + "' already exists.")
                        ))
                        .switchIfEmpty(Mono.defer(() -> branchRepository.save(
                                Branch.builder()
                                        .id(UUID.randomUUID().toString())
                                        .name(branchName)
                                        .franchiseId(franchiseId)
                                        .build()
                        )))
                )
                .doOnSuccess(saved -> log.info("Branch created successfully: " + saved));
    }

    private Mono<Boolean> validate(String branchName, String franchiseId) {
        boolean isValid = Objects.nonNull(branchName) && !branchName.isBlank() &&
                Objects.nonNull(franchiseId) && !franchiseId.isBlank();
        return Mono.just(isValid);
    }
}
