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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Store store;
    private Category category;

    @BeforeEach
    void setUp() {
        product = new Product();
        store = new Store();
        category = new Category();

        product.setName("test");
        product.setDescription("description");
        product.setPrice(new BigDecimal("10.2"));
        product.setStock(50);
        product.setLowStockThreshold(10);
        product.setStatus(ProductStatus.AVAILABLE);
        product.setMaxQuantity(100);
        product.setMinQuantity(5);
        product.setStore(store);
        product.setCategory(category);

        store.setId(1L);
        category.setId(1L);
        category.setStore(store);
        product.setId(1L);
    }

    @Test
    void getProducts_withBothFilters() {
        when(productRepository.findByStoreIdAndCategoryIdAndStatus(1L,
                1L,
                ProductStatus.AVAILABLE))
                .thenReturn(List.of(product));

        List<ProductDto> productDtoList = productService.getProducts(1L, 1L, ProductStatus.AVAILABLE);

        assertEquals(1, productDtoList.size());
        assertEquals(1L, productDtoList.get(0).categoryId());
        assertEquals(ProductStatus.AVAILABLE, productDtoList.get(0).status());
    }

    @Test
    void getProducts_withCategoryFilter() {
        when(productRepository.findByStoreIdAndCategoryId(1L, 1L)).thenReturn(List.of(product));

        List<ProductDto> productDtoList = productService.getProducts(1L,
                1L, null);

        assertEquals(1, productDtoList.size());
        assertEquals(1L, productDtoList.get(0).categoryId());
    }

    @Test
    void getProducts_withStatusFilters() {
        when(productRepository.findByStoreIdAndStatus(1L,
                ProductStatus.AVAILABLE)).thenReturn(List.of(product));

        List<ProductDto> productDtoList = productService.getProducts(1L,
                null, ProductStatus.AVAILABLE);

        assertEquals(1, productDtoList.size());
        assertEquals("test", productDtoList.get(0).name());
    }

    @Test
    void getProducts_withoutFilters() {
        when(productRepository.findByStoreId(1L)).thenReturn(List.of(product));

        List<ProductDto> productDtoList = productService.getProducts(1L,
                null, null);

        assertEquals(1, productDtoList.size());
        assertEquals("test", productDtoList.get(0).name());
    }

    @Test
    void getProduct_shouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductDto productDto = productService.getProduct(1L, 1L);

        assertEquals(1L, productDto.id());
        assertEquals("test", productDto.name());
    }

    @Test
    void getProduct_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,
                () -> productService.getProduct(1L, 1L));
    }

    @Test
    void getProduct_shouldThrowException_whenProductBelongsToDifferentStore() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        assertThrows(ProductNotFoundException.class,
                () -> productService.getProduct(99L, 1L));
    }

    @Test
    void createProduct_shouldCreateProduct() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductCreateRequest request = new ProductCreateRequest(
                "test",
                "description",
                new BigDecimal("100.22"),
                50,
                10,
                ProductStatus.AVAILABLE,
                5,
                100,
                1L
        );

        ProductDto result = productService.createProduct(1L, request);
        assertEquals("test", result.name());
    }

    @Test
    void createProduct_shouldThrowException_whenStoreNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());
        ProductCreateRequest request = new ProductCreateRequest(
                "test",
                "description",
                new BigDecimal("100.22"),
                50,
                10,
                ProductStatus.AVAILABLE,
                5,
                100,
                1L
        );
        assertThrows(StoreNotFoundException.class,
                () -> productService.createProduct(1L, request));
    }

    @Test
    void createProduct_shouldThrowException_whenCategoryNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        ProductCreateRequest request = new ProductCreateRequest(
                "test",
                "description",
                new BigDecimal("100.22"),
                50,
                10,
                ProductStatus.AVAILABLE,
                5,
                100,
                1L
        );
        assertThrows(CategoryNotFoundException.class,
                () -> productService.createProduct(1L, request));
    }

    @Test
    void createProduct_shouldThrowException_whenCategoryBelongsToDifferentStore() {
        when(storeRepository.findById(99L)).thenReturn(Optional.of(store));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        ProductCreateRequest request = new ProductCreateRequest(
                "test",
                "description",
                new BigDecimal("100.22"),
                50,
                10,
                ProductStatus.AVAILABLE,
                5,
                100,
                1L
        );

        assertThrows(CategoryNotFoundException.class,
                () -> productService.createProduct(99L, request));

    }

    @Test
    void updateProduct_shouldUpdateProduct() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductCreateRequest updatedProduct = new ProductCreateRequest(
                "test",
                "description",
                new BigDecimal("100.22"),
                50,
                10,
                ProductStatus.AVAILABLE,
                5,
                100,
                1L
        );

        ProductDto result = productService.updateProduct(1L, 1L, updatedProduct);
        assertEquals("test", result.name());
    }

    @Test
    void updateProduct_shouldTrowException_whenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        ProductCreateRequest updatedProduct = new ProductCreateRequest(
                "test",
                "description",
                new BigDecimal("100.22"),
                50,
                10,
                ProductStatus.AVAILABLE,
                5,
                100,
                1L
        );

        assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(1L, 1L, updatedProduct)
        );
    }

    @Test
    void updateProduct_shouldThrowException_whenProductBelongsToDifferentStore() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductCreateRequest updatedProduct = new ProductCreateRequest(
                "test",
                "description",
                new BigDecimal("100.22"),
                50,
                10,
                ProductStatus.AVAILABLE,
                5,
                100,
                1L
        );

        assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(99L, 1L, updatedProduct));
    }

    @Test
    void updateProduct_shouldThrowException_whenCategoryNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ProductCreateRequest updatedProduct = new ProductCreateRequest(
                "test",
                "description",
                new BigDecimal("100.22"),
                50,
                10,
                ProductStatus.AVAILABLE,
                5,
                100,
                1L
        );

        assertThrows(CategoryNotFoundException.class,
                () -> productService.updateProduct(1L, 1L, updatedProduct));
    }

    @Test
    void updateProduct_shouldThrowException_whenCategoryBelongsToDifferentStore() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Store otherStore = new Store();
        otherStore.setId(2L);
        category.setStore(otherStore); // переопределяем магазин категории — только для этого теста

        ProductCreateRequest updatedProduct = new ProductCreateRequest(
                "test",
                "description",
                new BigDecimal("100.22"),
                50,
                10,
                ProductStatus.AVAILABLE,
                5,
                100,
                1L
        );

        assertThrows(CategoryNotFoundException.class,
                () -> productService.updateProduct(1L, 1L, updatedProduct));
    }

    @Test
    void deleteProduct_shouldDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.deleteProduct(1L, 1L);
        verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L, 1L));
    }

    @Test
    void deleteProduct_shouldThrowException_whenProductBelongsToDifferentStore() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(99L, 1L));
    }
}
