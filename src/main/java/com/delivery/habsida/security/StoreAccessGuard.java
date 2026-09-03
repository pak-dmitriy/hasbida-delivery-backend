package com.delivery.habsida.security;

import com.delivery.habsida.repository.UserStoreAccessRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class StoreAccessGuard {
    private final UserStoreAccessRepository userStoreAccessRepository;

    public StoreAccessGuard(UserStoreAccessRepository userStoreAccessRepository) {
        this.userStoreAccessRepository = userStoreAccessRepository;
    }

    public boolean canAccessStore(Authentication authentication, Long storeId) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return true;
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userStoreAccessRepository.existsByUserIdAndStoreId(userPrincipal.userId(),  storeId);
    }
}
