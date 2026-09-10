package com.delivery.habsida.repository;

import com.delivery.habsida.entity.Product;
import com.delivery.habsida.entity.ProductStatus;
import com.delivery.habsida.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByStoreId(Long storeId);

    List<Product> findByStoreIdAndCategoryId(Long storeId, Long categoryId);

    List<Product> findByStoreIdAndStatus(Long storeId, ProductStatus status);

    List<Product> findByStoreIdAndCategoryIdAndStatus(Long storeId, Long categoryId, ProductStatus status);

}
