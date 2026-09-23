package com.delivery.habsida.controller;

import com.delivery.habsida.dto.ProductModifierGroupDto;
import com.delivery.habsida.exception.ProductModifierGroupAlreadyExistsException;
import com.delivery.habsida.exception.ProductModifierGroupNotFoundException;
import com.delivery.habsida.security.StoreAccessGuard;
import com.delivery.habsida.service.ProductModifierGroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductModifierGroupControllerTest {
    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductModifierGroupService productModifierGroupService;

    @MockitoBean
    private StoreAccessGuard storeAccessGuard;

    @BeforeEach
    void setUp() {
        when(storeAccessGuard.canAccessStore(any(), anyLong())).thenReturn(true);
    }

    @WithMockUser
    @Test
    void createProductModifierGroup_success() throws Exception {
        ProductModifierGroupDto dto = new ProductModifierGroupDto(
                1L,
                1L,
                1L
        );
        when(productModifierGroupService.createProductModifierGroup(anyLong(), anyLong(), anyLong())).thenReturn(dto);
        mockMvc.perform(MockMvcRequestBuilders.post("/stores/{storeId}/products/{productId}/modifier-groups/{groupId}", 1L, 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @WithMockUser
    @Test
    void createProductModifierGroup_shouldReturn409_whenDuplicate() throws Exception {
        when(productModifierGroupService.createProductModifierGroup(anyLong(), anyLong(), anyLong())).thenThrow(new ProductModifierGroupAlreadyExistsException(" ProductModifierGroup already exists"));
        mockMvc.perform(MockMvcRequestBuilders.post("/stores/{storeId}/products/{productId}/modifier-groups/{groupId}", 1L, 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @WithMockUser
    @Test
    void deleteProductModifierGroup_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/stores/{storeId}/products/{productId}/modifier-groups/{groupId}", 1L, 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Test
    void deleteProductModifierGroup_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new ProductModifierGroupNotFoundException("Group not found"))
                .when(productModifierGroupService).deleteProductModifierGroup(anyLong(), anyLong(), anyLong());
        mockMvc.perform(delete("/stores/{storeId}/products/{productId}/modifier-groups/{groupId}", 1L, 1L, 1L))
                .andExpect(status().isNotFound());
    }
}
