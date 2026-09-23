package com.delivery.habsida.service;

import com.delivery.habsida.dto.ProductModifierGroupDto;
import com.delivery.habsida.entity.ModifierGroup;
import com.delivery.habsida.entity.Product;
import com.delivery.habsida.entity.ProductModifierGroup;
import com.delivery.habsida.entity.Store;
import com.delivery.habsida.exception.ModifierGroupNotFoundException;
import com.delivery.habsida.exception.ProductModifierGroupAlreadyExistsException;
import com.delivery.habsida.repository.ModifierGroupRepository;
import com.delivery.habsida.repository.ProductModifierGroupRepository;
import com.delivery.habsida.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductModifierGroupServiceTest {
    @Mock
    private ProductModifierGroupRepository productModifierGroupRepository;

    @Mock
    private ModifierGroupRepository modifierGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductModifierGroupService productModifierGroupService;

    private ProductModifierGroup productModifierGroup;
    private ModifierGroup modifierGroup;
    private Product product;
    private Store store;

    @BeforeEach
    void setUp() {
        store = new Store();
        store.setId(1L);

        modifierGroup = new ModifierGroup();
        modifierGroup.setId(1L);
        modifierGroup.setStore(store);

        product = new Product();
        product.setId(1L);
        product.setStore(store);

        productModifierGroup = new ProductModifierGroup();
        productModifierGroup.setId(1L);
        productModifierGroup.setProduct(product);
        productModifierGroup.setModifierGroup(modifierGroup);
    }

    @Test
    void createProductModifierGroup_success() {
        when(productRepository.findByIdAndStoreId(1L, 1L)).thenReturn(Optional.of(product));
        when(modifierGroupRepository.findByIdAndStoreId(1L, 1L)).thenReturn(Optional.of(modifierGroup));
        when(productModifierGroupRepository.existsByProductIdAndModifierGroupId(1L, 1L)).thenReturn(false);
        when(productModifierGroupRepository.save(any())).thenReturn(productModifierGroup);

        ProductModifierGroupDto dto = productModifierGroupService.createProductModifierGroup(1L, 1L, 1L);
        assertEquals(1L, dto.id());
    }

    @Test
    void createProductModifierGroup_duplicateFound_throwsException() {
        when(productRepository.findByIdAndStoreId(1L, 1L)).thenReturn(Optional.of(product));
        when(modifierGroupRepository.findByIdAndStoreId(1L, 1L)).thenReturn(Optional.of(modifierGroup));
        when(productModifierGroupRepository.existsByProductIdAndModifierGroupId(1L, 1L)).thenReturn(true);

        assertThrows(ProductModifierGroupAlreadyExistsException.class,
                () -> productModifierGroupService.createProductModifierGroup(1L, 1L, 1L));
    }

    @Test
    void deleteProductModifierGroup_success() {
        when(productRepository.findByIdAndStoreId(1L, 1L)).thenReturn(Optional.of(product));
        when(modifierGroupRepository.findByIdAndStoreId(1L, 1L)).thenReturn(Optional.of(modifierGroup));
        when(productModifierGroupRepository.findByProductIdAndModifierGroupId(1L, 1L)).thenReturn(Optional.of(productModifierGroup));

        productModifierGroupService.deleteProductModifierGroup(1L, 1L, 1L);
        verify(productModifierGroupRepository).delete(productModifierGroup);
    }

    @Test
    void deleteProductModifierGroup_groupNotFound_throwsException() {
        when(productRepository.findByIdAndStoreId(1L, 1L)).thenReturn(Optional.of(product));
        when(modifierGroupRepository.findByIdAndStoreId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ModifierGroupNotFoundException.class,
                () -> productModifierGroupService.deleteProductModifierGroup(1L, 1L, 1L));
    }
}
