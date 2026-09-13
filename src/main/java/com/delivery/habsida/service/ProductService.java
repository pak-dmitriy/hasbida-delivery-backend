package com.delivery.habsida.service;

import com.delivery.habsida.dto.ProductCreateRequest;
import com.delivery.habsida.dto.ProductDto;
import com.delivery.habsida.entity.Category;
import com.delivery.habsida.entity.Product;
import com.delivery.habsida.entity.ProductStatus;
import com.delivery.habsida.entity.Store;
import com.delivery.habsida.exception.CategoryNotFoundException;
import com.delivery.habsida.exception.ProductNotFoundException;
import com.delivery.habsida.exception.StoreNotFoundException;
import com.delivery.habsida.repository.CategoryRepository;
import com.delivery.habsida.repository.ProductRepository;
import com.delivery.habsida.repository.StoreRepository;
import com.delivery.habsida.security.StoreAccessGuard;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, StoreRepository storeRepository, CategoryRepository categoryRepository, StoreAccessGuard storeAccessGuard) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    public ProductDto createProduct(Long storeId, ProductCreateRequest request) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("Store not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        if (!category.getStore().getId().equals(storeId)) {
            throw new CategoryNotFoundException("Category not found");
        }

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setLowStockThreshold(request.lowStockThreshold());
        product.setMaxQuantity(request.maxQuantity());
        product.setMinQuantity(request.minQuantity());
        product.setStatus(request.status());
        product.setStore(store);
        product.setCategory(category);

        return ProductDto.from(productRepository.save(product));
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    public List<ProductDto> getProducts(Long storeId, Long categoryId, ProductStatus status) {

        if(categoryId != null && status != null) {
            return productRepository.findByStoreIdAndCategoryIdAndStatus(storeId, categoryId, status).stream().map(ProductDto::from).toList();
        } else if(categoryId != null){
            return productRepository.findByStoreIdAndCategoryId(storeId, categoryId).stream().map(ProductDto::from).toList();
        } else if(status != null) {
            return productRepository.findByStoreIdAndStatus(storeId, status).stream().map(ProductDto::from).toList();
        } else {
            return productRepository.findByStoreId(storeId).stream().map(ProductDto::from).toList();
        }
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    public ProductDto getProduct( Long storeId, Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (!product.getStore().getId().equals(storeId)) {
            throw new ProductNotFoundException("Product not found");
        }

        return ProductDto.from(product);
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    public ProductDto updateProduct(Long storeId, Long productId, ProductCreateRequest request) {

        Product product = productRepository.findById(productId)   // Шаг 1
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (!product.getStore().getId().equals(storeId)) {            // Шаг 2
            throw new ProductNotFoundException("Product not found");
        }
        Category category = categoryRepository.findById(request.categoryId())  // Шаг 3
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        if (!category.getStore().getId().equals(storeId)) {             // Шаг 4
            throw new CategoryNotFoundException("Category not found");
        }
        product.setName(request.name());                                // Шаг 5
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setLowStockThreshold(request.lowStockThreshold());
        product.setMaxQuantity(request.maxQuantity());
        product.setMinQuantity(request.minQuantity());
        product.setStatus(request.status());
        product.setCategory(category);

        return ProductDto.from(productRepository.save(product)); // Шаг 6
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    public void deleteProduct( Long storeId, Long productId) {
        Product product = productRepository.findById(productId) // Шаг 1
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (!product.getStore().getId().equals(storeId)) {          // Шаг 2
            throw new ProductNotFoundException("Product not found");
        }

        productRepository.delete(product);
    }


}

