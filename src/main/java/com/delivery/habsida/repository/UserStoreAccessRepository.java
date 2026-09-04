package com.delivery.habsida.repository;

import com.delivery.habsida.entity.UserStoreAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserStoreAccessRepository extends JpaRepository<UserStoreAccess, Long> {
boolean existsByUserIdAndStoreId(Long userId, Long storeId);
List<UserStoreAccess> findByUserId(Long userId);
}
