package com.delivery.habsida.service;

import com.delivery.habsida.dto.StoreCreateRequest;
import com.delivery.habsida.dto.StoreCreateResponse;
import com.delivery.habsida.dto.StoreResponse;
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

    public StoreService(StoreRepository storeRepository, UserStoreAccessRepository userStoreAccessRepository) {
        this.storeRepository = storeRepository;
        this.userStoreAccessRepository = userStoreAccessRepository;
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    public StoreResponse getStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("Store not found"));
        return toResponse(store);
    }

    public List<StoreResponse> getStores(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return storeRepository.findAll().stream()
                    .map(this::toResponse)
                    .toList();
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<UserStoreAccess> userStoreAccesses = userStoreAccessRepository.findByUserId(userPrincipal.userId());

        return userStoreAccesses.stream()
                .map(UserStoreAccess::getStore)
                .map(this::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public StoreCreateResponse createStore(StoreCreateRequest storeCreateRequest) {
        // 1. Создаем сущность из реквеста
        Store store = new Store();

        store.setName(storeCreateRequest.name());
        store.setStoreSlug(storeCreateRequest.storeSlug());
        store.setDescription(storeCreateRequest.description());
        store.setTypeStoreServices(storeCreateRequest.typeStoreServices());
        store.setPhone(storeCreateRequest.phone());
        store.setLogo(storeCreateRequest.logo());
        store.setStatus(storeCreateRequest.status());
        store.setPickupAddress(storeCreateRequest.pickupAddress());

        // 2. Сохраняем в базу (получаем ID)
        Store savedStore = storeRepository.save(store);

        return new StoreCreateResponse(
                savedStore.getId(),
                savedStore.getName(),
                savedStore.getStoreSlug(),
                savedStore.getDescription(),
                savedStore.getTypeStoreServices(),
                savedStore.getPhone(),
                savedStore.getLogo(),
                savedStore.getStatus(),
                savedStore.getPickupAddress()
        );
    }

    private StoreResponse toResponse(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getStoreSlug(),
                store.getDescription(),
                store.getTypeStoreServices(),
                store.getPhone(),
                store.getLogo(),
                store.getStatus(),
                store.getPickupAddress()

        );
    }

}
