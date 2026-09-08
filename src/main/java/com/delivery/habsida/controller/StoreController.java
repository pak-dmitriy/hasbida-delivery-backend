package com.delivery.habsida.controller;

import com.delivery.habsida.dto.StoreCreateRequest;
import com.delivery.habsida.dto.StoreCreateResponse;
import com.delivery.habsida.dto.StoreResponse;
import com.delivery.habsida.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    @GetMapping
    public List<StoreResponse> getStores(Authentication authentication) {
        return storeService.getStores(authentication);
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    @GetMapping("/{storeId}")
    public StoreResponse getStore(@PathVariable Long storeId) {
        return storeService.getStore(storeId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StoreCreateResponse> createStore(@RequestBody @Valid StoreCreateRequest storeCreateRequest) {
        StoreCreateResponse response = storeService.createStore(storeCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
