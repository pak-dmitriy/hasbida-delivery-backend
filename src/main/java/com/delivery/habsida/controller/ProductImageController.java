package com.delivery.habsida.controller;

import com.delivery.habsida.dto.ProductImagesDto;
import com.delivery.habsida.dto.ProductImagesRequest;
import com.delivery.habsida.service.ProductImagesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductImageController {

    private final ProductImagesService productImagesService;

    public ProductImageController(ProductImagesService productImagesService) {
        this.productImagesService = productImagesService;
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    @PostMapping("/stores/{storeId}/products/{productId}/images")
    public ResponseEntity<ProductImagesDto> createProductImage(@PathVariable Long storeId,
                                                               @PathVariable Long productId,
                                                               @RequestBody @Valid ProductImagesRequest request) {

        ProductImagesDto created = productImagesService.createImages(storeId, productId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    @GetMapping("/stores/{storeId}/products/{productId}/images")
    public List<ProductImagesDto> getProductImages(@PathVariable Long storeId,
                                                   @PathVariable Long productId) {

        return productImagesService.getProductImages(storeId, productId);
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    @DeleteMapping("/stores/{storeId}/products/{productId}/images/{imageId}")
    public ResponseEntity<Void> deleteProductImages(@PathVariable Long storeId,
                                                    @PathVariable Long productId,
                                                    @PathVariable Long imageId) {

        productImagesService.deleteProductImages(storeId, productId, imageId);

        return ResponseEntity.noContent().build();
    }
}
