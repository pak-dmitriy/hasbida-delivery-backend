package com.delivery.habsida.controller;

import com.delivery.habsida.entity.Store;
import com.delivery.habsida.service.StoreService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }


    @GetMapping("/stores")
    public String getStores() {
        return "stores";
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    @GetMapping("/stores/{storeId}")
    public Store getStore(@PathVariable Long storeId) {
        return storeService.getStore(storeId);
    }

}
