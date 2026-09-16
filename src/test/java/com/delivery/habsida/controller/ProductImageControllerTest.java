package com.delivery.habsida.controller;

import com.delivery.habsida.dto.ProductImagesDto;
import com.delivery.habsida.dto.ProductImagesRequest;
import com.delivery.habsida.security.StoreAccessGuard;
import com.delivery.habsida.service.ProductImagesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductImagesService productImagesService;

    @MockitoBean
    private StoreAccessGuard storeAccessGuard;

    @WithMockUser
    @Test
    void createProductImage_shouldReturn201_whenRequestIsValid() throws Exception {

        when(storeAccessGuard.canAccessStore(
                any(),
                anyLong())).thenReturn(true);

        ProductImagesDto dto = new ProductImagesDto(1L, "imagePhoto", 5);

        when(productImagesService.createImages(anyLong(),
                anyLong(),
                any())).thenReturn(dto);

        ProductImagesRequest request = new ProductImagesRequest(
                "imagePhoto",
                5
        );

        mockMvc.perform(post("/stores/{storeId}/products/{productId}/images", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @WithMockUser
    @Test
    void createProductImage_shouldReturn400_whenImagePhotoIsBlank() throws Exception {
        when(storeAccessGuard.canAccessStore(
                any(),
                anyLong())).thenReturn(true);

        ProductImagesRequest invalidRequest = new ProductImagesRequest(" ", 5);

        mockMvc.perform(post("/stores/{storeId}/products/{productId}/images", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    void createProductImage_shouldReturn400_whenSortOrderIsBlank() throws Exception {
        when(storeAccessGuard.canAccessStore(any(), anyLong())).thenReturn(true);

        ProductImagesRequest invalidRequest = new ProductImagesRequest(
                "imagePhoto",
                -5
        );

        mockMvc.perform(post("/stores/{storeId}/products/{productId}/images", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    void getProductImages_shouldReturnListOfProductImages() throws Exception {

        when(storeAccessGuard.canAccessStore(any(), anyLong())).thenReturn(true);
        ProductImagesDto dto = new ProductImagesDto(
                1L,
                "imagePhoto",
                5
        );
        when(productImagesService.getProductImages(
                anyLong(),
                anyLong())).thenReturn(List.of(dto));

        mockMvc.perform(get("/stores/{storeId}/products/{productId}/images", 1L, 1L))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void deleteProductImages_shouldReturn204_whenRequestIsValid() throws Exception {
        when(storeAccessGuard.canAccessStore(
                any(),
                anyLong())).thenReturn(true);

        mockMvc.perform(delete("/stores/{storeId}/products/{productId}/images/{imageId}",
                        1L, 1L, 1L))
                .andExpect(status().isNoContent());
    }
}
