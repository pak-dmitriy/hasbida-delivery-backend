package com.delivery.habsida.repository;

import com.delivery.habsida.entity.User;
import com.delivery.habsida.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser(User user);
}
