package com.delivery.habsida.repository;

import com.delivery.habsida.entity.UserStoreAccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoreAccessRepository extends JpaRepository<UserStoreAccess, Long> {
boolean existsByUserIdAndStoreId(Long userId, Long storeId);
}
