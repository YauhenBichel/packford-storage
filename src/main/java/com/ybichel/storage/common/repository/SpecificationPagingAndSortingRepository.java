package com.ybichel.storage.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface SpecificationPagingAndSortingRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
