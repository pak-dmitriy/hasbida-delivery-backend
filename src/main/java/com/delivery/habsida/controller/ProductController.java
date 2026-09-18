package com.delivery.habsida.controller;

import com.delivery.habsida.dto.ProductCreateRequest;
import com.delivery.habsida.dto.ProductDto;
import com.delivery.habsida.entity.ProductStatus;
import com.delivery.habsida.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/stores/{storeId}/products")
    public ResponseEntity<ProductDto> createProduct(@PathVariable Long storeId,
                                                    @RequestBody @Valid ProductCreateRequest request) {

        ProductDto created = productService.createProduct(storeId, request); // Step 1

        return ResponseEntity.status(HttpStatus.CREATED).body(created);  // Step 2
    }

    @GetMapping("/stores/{storeId}/products")
    public List<ProductDto> getProducts(@PathVariable Long storeId,
                                        @RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) ProductStatus status) {

        return productService.getProducts(storeId, categoryId, status);
    }

    @GetMapping("/stores/{storeId}/products/{productId}")
    public ProductDto getProduct(@PathVariable Long storeId,
                                 @PathVariable Long productId) {

        return productService.getProduct(storeId, productId);
    }

    @PutMapping("/stores/{storeId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long storeId,
                                                    @PathVariable Long productId,
                                                    @RequestBody @Valid ProductCreateRequest request) {
        ProductDto updatedProduct = productService.updateProduct(storeId, productId, request);
        return ResponseEntity.ok().body(updatedProduct);
    }

    @DeleteMapping("/stores/{storeId}/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long storeId,
                                              @PathVariable Long productId) {

        productService.deleteProduct(storeId, productId);

        return ResponseEntity.noContent().build();
    }


}
