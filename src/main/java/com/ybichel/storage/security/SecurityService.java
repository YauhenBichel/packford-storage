package com.ybichel.storage.security;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.security.entity.StorageRole;

import java.util.Set;

public interface SecurityService {

    /**
     * @return current user context
     */
    Account getUser();

    /**
     * @return current user roles or empty
     */
    Set<StorageRole> getRoles();

    /**
     * @return current user principal
     */
    String getPrincipal();

    /**
     * @param roles roles
     * @return true if current user has any of specified roles
     */
    boolean hasAnyRoles(String... roles);
}
