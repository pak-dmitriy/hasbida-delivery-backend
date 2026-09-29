package com.delivery.habsida.repository;

import com.delivery.habsida.entity.Product;
import com.delivery.habsida.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(value = "UPDATE products SET stock = stock - :quantity WHERE id = :productId AND stock >= :quantity RETURNING stock",
    nativeQuery = true)
    List<Integer> decreaseStockAndGet(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    List<Product> findByStoreId(Long storeId);

    List<Product> findByStoreIdAndCategoryId(Long storeId, Long categoryId);

    List<Product> findByStoreIdAndStatus(Long storeId, ProductStatus status);

    List<Product> findByStoreIdAndCategoryIdAndStatus(Long storeId, Long categoryId, ProductStatus status);

    Optional<Product> findByIdAndStoreId(Long productId, Long storeId);
}
