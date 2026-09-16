package com.delivery.habsida.service;

import com.delivery.habsida.dto.ProductImagesDto;
import com.delivery.habsida.dto.ProductImagesRequest;
import com.delivery.habsida.entity.Product;
import com.delivery.habsida.entity.ProductImages;
import com.delivery.habsida.exception.ImageNotFoundException;
import com.delivery.habsida.exception.ProductNotFoundException;
import com.delivery.habsida.repository.ProductImagesRepository;
import com.delivery.habsida.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImagesService {
    private final ProductImagesRepository productImagesRepository;
    private final ProductRepository productRepository;

    public ProductImagesService(ProductImagesRepository productImagesRepository, ProductRepository productRepository) {
        this.productImagesRepository = productImagesRepository;
        this.productRepository = productRepository;
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    public ProductImagesDto createImages(Long storeId, Long productId, ProductImagesRequest request) {

        Product product = productRepository.findById(productId)  // Шаг 1
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (!product.getStore().getId().equals(storeId)) {          // Шаг2
            throw new ProductNotFoundException("Product not found");
        }

        ProductImages productImages = new ProductImages();
        productImages.setImagePhoto(request.imagePhoto());
        productImages.setSortOrder(request.sortOrder());
        productImages.setProduct(product);

        return ProductImagesDto.from(productImagesRepository.save(productImages));  // Шаг 3
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    public List<ProductImagesDto> getProductImages(Long storeId, Long productId) {

        // проверить, что товар существует и принадлежит storeId
        Product product = productRepository.findById(productId)  // Step 1
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (!product.getStore().getId().equals(storeId)) {             // Step 2
            throw new ProductNotFoundException("Product not found");
        }
        return productImagesRepository.findByProductId(productId).stream() // Step 3
                .map(ProductImagesDto::from).toList();
    }

    @PreAuthorize("@storeAccessGuard.canAccessStore(authentication, #storeId)")
    public void deleteProductImages(Long storeId, Long productId, Long imageId) {

        Product product = productRepository.findById(productId)         // Step 1
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (!product.getStore().getId().equals(storeId)) {                  // step 2
            throw new ProductNotFoundException("Product not found");
        }

        ProductImages image = productImagesRepository.findById(imageId)     // Step 3
                .orElseThrow(() -> new ImageNotFoundException("Image not found"));
        if(!image.getProduct().getId().equals(productId)) {                 // Step 4
            throw new ImageNotFoundException("Image not found");
        }

        productImagesRepository.delete(image);                  // Step 5

    }
}
