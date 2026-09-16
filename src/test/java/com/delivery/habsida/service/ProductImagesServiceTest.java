package com.delivery.habsida.service;

import com.delivery.habsida.dto.ProductImagesDto;
import com.delivery.habsida.dto.ProductImagesRequest;
import com.delivery.habsida.entity.Product;
import com.delivery.habsida.entity.ProductImages;
import com.delivery.habsida.entity.Store;
import com.delivery.habsida.exception.ImageNotFoundException;
import com.delivery.habsida.exception.ProductNotFoundException;
import com.delivery.habsida.repository.ProductImagesRepository;
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
class ProductImagesServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImagesRepository productImagesRepository;

    @InjectMocks
    private ProductImagesService productImagesService;

    private Store store;
    private Product product;
    private ProductImages productImages;

    @BeforeEach
    void setUp() {

        store = new Store();
        product = new Product();
        productImages = new ProductImages();

        store.setId(1L);
        product.setId(1L);
        product.setStore(store);

        productImages.setProduct(product);
        productImages.setImagePhoto("apple.png");
        productImages.setSortOrder(5);
        productImages.setId(1L);

    }

    @Test
    void createProductImages_shouldCreateProductImages() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImagesRepository.save(any(ProductImages.class))).thenReturn(productImages);

        ProductImagesRequest request = new ProductImagesRequest(

                "imagePhoto",
                11
        );

        ProductImagesDto result = productImagesService.createImages(1L, 1L, request);
        assertEquals("apple.png", result.imagePhoto());
        assertEquals(5, result.sortOrder());

    }

    @Test
    void createProductImages_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductImagesRequest request = new ProductImagesRequest(

                "imagePhoto",
                11
        );

        assertThrows(ProductNotFoundException.class,
                () -> productImagesService.createImages(1L, 1L, request));

    }

    @Test
    void createProductImages_shouldThrowException_whenProductBelongsToDifferentStore() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductImagesRequest request = new ProductImagesRequest(

                "imagePhoto",
                11
        );

        assertThrows(ProductNotFoundException.class,
                () -> productImagesService.createImages(99L, 1L, request));
    }

    @Test
    void getProductImages_shouldReturnListOfImages() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImagesRepository.findByProductId(1L))
                .thenReturn(List.of(productImages));

        List<ProductImagesDto> list = productImagesService.getProductImages(1L, 1L);

        assertEquals(1, list.size());
        assertEquals("apple.png", list.get(0).imagePhoto());
    }

    @Test
    void getProductImages_shouldThrowException_whenProductNotFound() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,
                () -> productImagesService.getProductImages(1L, 1L));

    }

    @Test
    void getProductImages_shouldThrowException_whenProductBelongsToDifferentStore() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertThrows(ProductNotFoundException.class,
                () -> productImagesService.getProductImages(99L, 1L));
    }

    @Test
    void deleteProductImages_shouldDeleteProductImages() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImagesRepository.findById(1L)).thenReturn(Optional.of(productImages));
        productImagesService.deleteProductImages(1L, 1L, 1L);
        verify(productImagesRepository).delete(productImages);
    }

    @Test
    void deleteProductImages_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,
                () -> productImagesService.deleteProductImages(1L, 1L, 1L));
    }

    @Test
    void deleteProductImages_shouldThrowException_whenProductBelongsToDifferentStore() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertThrows(ProductNotFoundException.class,
                () -> productImagesService.deleteProductImages(99L, 1L, 1L));
    }

    @Test
    void deleteProductImages_shouldThrowException_whenProductImagesNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImagesRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ImageNotFoundException.class,
                () -> productImagesService.deleteProductImages(1L, 1L, 1L));
    }

    @Test
    void deleteProductImages_shouldThrowException_whenImagesBelongsToDifferentProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productImagesRepository.findById(1L)).thenReturn(Optional.of(productImages));

        Product otherProduct = new Product();
        otherProduct.setId(2L);
        productImages.setProduct(otherProduct);

        assertThrows(ImageNotFoundException.class,
                () -> productImagesService.deleteProductImages(1L, 1L, 1L));
    }
}
