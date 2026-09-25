package com.delivery.habsida.controller;

import com.delivery.habsida.dto.ProductModifierGroupDto;
import com.delivery.habsida.service.ProductModifierGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores/{storeId}/products/{productId}/modifier-groups/{groupId}")
public class ProductModifierGroupController {
    private final ProductModifierGroupService productModifierGroupService;

    public ProductModifierGroupController(ProductModifierGroupService productModifierGroupService) {
        this.productModifierGroupService = productModifierGroupService;
    }

    @PostMapping
    public ResponseEntity<ProductModifierGroupDto> createProductModifierGroup(@PathVariable Long storeId,
                                                                              @PathVariable Long productId,
                                                                              @PathVariable Long groupId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productModifierGroupService.createProductModifierGroup(storeId, productId, groupId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProductModifierGroup(@PathVariable Long storeId,
                                                           @PathVariable Long productId,
                                                           @PathVariable Long groupId) {
        productModifierGroupService.deleteProductModifierGroup(storeId, productId, groupId);
        return ResponseEntity.noContent().build();
    }
}
