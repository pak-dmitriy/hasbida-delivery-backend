package com.delivery.habsida.service;

import com.delivery.habsida.dto.ProductImageDto;
import com.delivery.habsida.dto.ProductImageRequest;
import com.delivery.habsida.entity.Product;
import com.delivery.habsida.entity.ProductImage;
import com.delivery.habsida.entity.Store;
import com.delivery.habsida.exception.ImageNotFoundException;
import com.delivery.habsida.exception.ProductNotFoundException;
import com.delivery.habsida.repository.ProductImageRepository;
import com.delivery.habsida.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductImageServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @InjectMocks
    private ProductImageService productImageService;

    private Store store;
    private Product product;
    private ProductImage productImage;

    @BeforeEach
    void setUp() {

        store = new Store();
        product = new Product();
        productImage = new ProductImage();

        store.setId(1L);
        product.setId(1L);
        product.setStore(store);

        productImage.setProduct(product);
        productImage.setImagePhoto("apple.png");
        productImage.setSortOrder(5);
        productImage.setId(1L);

    }

    @Test
    void createProductImages_shouldCreateProductImages() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImageRepository.save(any(ProductImage.class))).thenReturn(productImage);

        ProductImageRequest request = new ProductImageRequest(

                "imagePhoto",
                11
        );

        ProductImageDto result = productImageService.createImages(1L, 1L, request);
        assertEquals("apple.png", result.imagePhoto());
        assertEquals(5, result.sortOrder());

    }

    @Test
    void createProductImages_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductImageRequest request = new ProductImageRequest(

                "imagePhoto",
                11
        );

        assertThrows(ProductNotFoundException.class,
                () -> productImageService.createImages(1L, 1L, request));

    }

    @Test
    void createProductImages_shouldThrowException_whenProductBelongsToDifferentStore() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductImageRequest request = new ProductImageRequest(

                "imagePhoto",
                11
        );

        assertThrows(ProductNotFoundException.class,
                () -> productImageService.createImages(99L, 1L, request));
    }

    @Test
    void getProductImages_shouldReturnListOfImages() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImageRepository.findByProductIdOrderBySortOrderAsc(1L))
                .thenReturn(List.of(productImage));

        List<ProductImageDto> list = productImageService.getProductImages(1L, 1L);

        assertEquals(1, list.size());
        assertEquals("apple.png", list.get(0).imagePhoto());
    }

    @Test
    void getProductImages_shouldThrowException_whenProductNotFound() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,
                () -> productImageService.getProductImages(1L, 1L));

    }

    @Test
    void getProductImages_shouldThrowException_whenProductBelongsToDifferentStore() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertThrows(ProductNotFoundException.class,
                () -> productImageService.getProductImages(99L, 1L));
    }

    @Test
    void deleteProductImages_shouldDeleteProductImages() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImageRepository.findById(1L)).thenReturn(Optional.of(productImage));
        productImageService.deleteProductImages(1L, 1L, 1L);
        verify(productImageRepository).delete(productImage);
    }

    @Test
    void deleteProductImages_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,
                () -> productImageService.deleteProductImages(1L, 1L, 1L));
    }

    @Test
    void deleteProductImages_shouldThrowException_whenProductBelongsToDifferentStore() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertThrows(ProductNotFoundException.class,
                () -> productImageService.deleteProductImages(99L, 1L, 1L));
    }

    @Test
    void deleteProductImages_shouldThrowException_whenProductImagesNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImageRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ImageNotFoundException.class,
                () -> productImageService.deleteProductImages(1L, 1L, 1L));
    }

    @Test
    void deleteProductImages_shouldThrowException_whenImagesBelongsToDifferentProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImageRepository.findById(1L)).thenReturn(Optional.of(productImage));

        Product otherProduct = new Product();
        otherProduct.setId(2L);
        productImage.setProduct(otherProduct);

        assertThrows(ImageNotFoundException.class,
                () -> productImageService.deleteProductImages(1L, 1L, 1L));
    }
}
