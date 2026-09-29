package com.delivery.habsida.repository;

import com.delivery.habsida.entity.ModifierGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModifierGroupRepository extends JpaRepository<ModifierGroup, Long> {
    List<ModifierGroup> findByStoreId(Long storeId);
    Optional<ModifierGroup> findByIdAndStoreId(Long id, Long storeId);
}
