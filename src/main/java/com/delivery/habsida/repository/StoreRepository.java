package com.delivery.habsida.repository;

import com.delivery.habsida.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> id(Long id);
}
