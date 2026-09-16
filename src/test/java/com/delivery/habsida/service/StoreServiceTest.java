package com.delivery.habsida.service;

import com.delivery.habsida.dto.StoreCreateRequest;
import com.delivery.habsida.dto.StoreCreateResponse;
import com.delivery.habsida.dto.StoreResponse;
import com.delivery.habsida.entity.Status;
import com.delivery.habsida.entity.Store;
import com.delivery.habsida.entity.TypeStoreServices;
import com.delivery.habsida.exception.StoreNotFoundException;
import com.delivery.habsida.repository.StoreRepository;
import com.delivery.habsida.repository.UserStoreAccessRepository;
import com.delivery.habsida.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserStoreAccessRepository userStoreAccessRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private StoreService storeService;
    private StoreCreateRequest request;
    private Store store;

    @BeforeEach
    void setUp() {
        request = new StoreCreateRequest(
                "Sushi House",
                "sushi-house",
                "Best sushi in town",
                TypeStoreServices.RESTAURANT,
                "+998901234567",
                "logo.png",
                "Tashkent, Amir Temur 1",
                Status.ACTIVE
        );

        store = new Store();
        store.setId(2L);
        store.setName("Sushi House");
        store.setStoreSlug("sushi-house");
        store.setDescription("Best sushi in town");
        store.setTypeStoreServices(TypeStoreServices.RESTAURANT);
        store.setPhone("+998901234567");
        store.setLogo("logo.png");
        store.setPickupAddress("Tashkent, Amir Temur 1");
        store.setStatus(Status.ACTIVE);
    }

    // ---------- getStore ----------
    @Test
    void getStore_shouldReturnStore_whenStoreExist() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        StoreResponse result = storeService.getStore(1L);

        assertEquals("Sushi House", result.name());
        assertEquals("sushi-house", result.storeSlug());

    }

    @Test
    void getStore_shouldThrowException_whenStoreNotFound() {
        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> storeService.getStore(99L));
    }

    // ---------- getStores ----------
    @Test
    void getStores_shouldReturnAllStores_whenUserIsAdmin() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .when(authentication).getAuthorities();
        when(storeRepository.findAll()).thenReturn(List.of(store));

        List<StoreResponse> result = storeService.getStores(authentication);

        assertEquals(1, result.size());
        assertEquals("Sushi House", result.get(0).name());

    }

    @Test
    void getStores_shouldReturnEmptyList_whenMerchantHasNoStores() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_MERCHANT")))
                .when(authentication).getAuthorities();
        when(authentication.getPrincipal())
                .thenReturn(new UserPrincipal(3L, "merchant@merchant"));
        when(userStoreAccessRepository.findByUserId(3L)).thenReturn(List.of());

        List<StoreResponse> result = storeService.getStores(authentication);

        assertTrue(result.isEmpty());

    }

    // ---------- createStore ----------

    @Test
    void createStore_shouldCreateStoreAndReturnResponse() {
        when(storeRepository.save(any(Store.class))).thenReturn(store);

        StoreCreateResponse response = storeService.createStore(request);

        assertEquals(2L, response.id());
        assertEquals("Sushi House", response.name());
        assertEquals("sushi-house", response.storeSlug());
        assertEquals(TypeStoreServices.RESTAURANT, response.typeStoreServices());
        assertEquals(Status.ACTIVE, response.status());

        verify(storeRepository).save(any(Store.class));

    }
}