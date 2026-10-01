package com.delivery.habsida.service;

import com.delivery.habsida.dto.MeResponse;
import com.delivery.habsida.dto.MeStoreDto;
import com.delivery.habsida.entity.UserStoreAccess;
import com.delivery.habsida.repository.UserStoreAccessRepository;
import com.delivery.habsida.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserStoreAccessRepository userStoreAccessRepository;

    public UserService(UserStoreAccessRepository userStoreAccessRepository) {
        this.userStoreAccessRepository = userStoreAccessRepository;
    }

    public MeResponse getMe(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        List<String> roles = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .map(role -> role.replace("ROLE_", ""))
                .toList();

        List<UserStoreAccess> accesses = userStoreAccessRepository.findByUserId(userPrincipal.userId());

        MeStoreDto store = null;
        if (!accesses.isEmpty()) {
            store = MeStoreDto.from(accesses.get(0).getStore());
        }
        return new MeResponse(userPrincipal.userId(), userPrincipal.email(),  roles, store);
    }
}
