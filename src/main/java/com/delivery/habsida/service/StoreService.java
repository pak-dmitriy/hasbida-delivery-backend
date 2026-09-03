package com.delivery.habsida.service;

import com.delivery.habsida.entity.Store;
import com.delivery.habsida.repository.StoreRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

private final StoreRepository storeRepository;
public StoreService(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
}

@PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
public Store getStore(Long storeId){
    return storeRepository.findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found"));}

}
