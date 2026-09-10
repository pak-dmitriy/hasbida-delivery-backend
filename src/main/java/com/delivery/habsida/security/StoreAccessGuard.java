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

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean canAccessStore(Authentication authentication, Long storeId) {
        if (isAdmin(authentication)) {
            return true;
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userStoreAccessRepository.existsByUserIdAndStoreId(userPrincipal.userId(), storeId);
    }
}
