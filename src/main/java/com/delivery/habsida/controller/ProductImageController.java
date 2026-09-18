package com.delivery.habsida.controller;

import com.delivery.habsida.dto.ProductImageDto;
import com.delivery.habsida.dto.ProductImageRequest;
import com.delivery.habsida.service.ProductImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping("/stores/{storeId}/products/{productId}/images")
    public ResponseEntity<ProductImageDto> createProductImage(@PathVariable Long storeId,
                                                              @PathVariable Long productId,
                                                              @RequestBody @Valid ProductImageRequest request) {

        ProductImageDto created = productImageService.createImages(storeId, productId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/stores/{storeId}/products/{productId}/images")
    public List<ProductImageDto> getProductImages(@PathVariable Long storeId,
                                                  @PathVariable Long productId) {

        return productImageService.getProductImages(storeId, productId);
    }

    @DeleteMapping("/stores/{storeId}/products/{productId}/images/{imageId}")
    public ResponseEntity<Void> deleteProductImages(@PathVariable Long storeId,
                                                    @PathVariable Long productId,
                                                    @PathVariable Long imageId) {

        productImageService.deleteProductImages(storeId, productId, imageId);

        return ResponseEntity.noContent().build();
    }
}
