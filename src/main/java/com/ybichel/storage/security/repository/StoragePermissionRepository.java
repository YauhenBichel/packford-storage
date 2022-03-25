package com.ybichel.storage.security.repository;

import com.ybichel.storage.common.repository.SpecificationPagingAndSortingRepository;
import com.ybichel.storage.security.entity.StoragePermission;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoragePermissionRepository extends SpecificationPagingAndSortingRepository<StoragePermission, UUID> {

}
