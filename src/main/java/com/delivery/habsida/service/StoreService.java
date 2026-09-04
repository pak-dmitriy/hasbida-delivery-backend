package com.delivery.habsida.service;

import com.delivery.habsida.entity.Store;
import com.delivery.habsida.entity.UserStoreAccess;
import com.delivery.habsida.exception.StoreNotFoundException;
import com.delivery.habsida.repository.StoreRepository;
import com.delivery.habsida.repository.UserStoreAccessRepository;
import com.delivery.habsida.security.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

private final StoreRepository storeRepository;
private final UserStoreAccessRepository userStoreAccessRepository;

public StoreService(StoreRepository storeRepository,  UserStoreAccessRepository userStoreAccessRepository) {
        this.storeRepository = storeRepository;
        this.userStoreAccessRepository = userStoreAccessRepository;
}

@PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
public Store getStore(Long storeId){
    return storeRepository.findById(storeId)
            .orElseThrow(() -> new StoreNotFoundException("Store not found"));}

    public List<Store> getStores(Authentication authentication){
    boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    if (isAdmin) {
        return storeRepository.findAll();
    }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    List<UserStoreAccess> userStoreAccesses = userStoreAccessRepository.findByUserId(userPrincipal.userId());

    return userStoreAccesses.stream()
            .map(UserStoreAccess::getStore)
            .toList();
    }

}
