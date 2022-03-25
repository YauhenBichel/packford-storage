package com.ybichel.storage.security.repository;

import com.ybichel.storage.common.repository.SpecificationPagingAndSortingRepository;
import com.ybichel.storage.security.entity.StorageRole;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StorageRoleRepository extends SpecificationPagingAndSortingRepository<StorageRole, UUID> {
    Optional<StorageRole> findTwoayRoleByNameEquals(String name);
}
