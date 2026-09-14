package com.delivery.habsida.service;

import com.delivery.habsida.dto.CustomerAddressCreateRequest;
import com.delivery.habsida.dto.CustomerAddressDTO;
import com.delivery.habsida.entity.Customer;
import com.delivery.habsida.entity.CustomerAddress;
import com.delivery.habsida.exception.CustomerAddressNotFoundException;
import com.delivery.habsida.repository.CustomerAddressRepository;
import com.delivery.habsida.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerAddressServiceTest {
    @Mock
    private CustomerAddressRepository customerAddressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerAddressService customerAddressService;

    private Customer customer;
    private CustomerAddress customerAddress;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customerAddress = new CustomerAddress();

        customerAddress.setId(1L);
        customerAddress.setCustomer(customer);
        customer.setId(1L);
        customerAddress.setCity("London");
        customerAddress.setStreet("Luna");
        customerAddress.setHouse("21");
        customerAddress.setApartment("2");
    }

    @Test
    void createAddress() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.save(any(CustomerAddress.class))).thenReturn(customerAddress);
        CustomerAddressCreateRequest request = new CustomerAddressCreateRequest("London", "Luna", "21", "2");
        CustomerAddressDTO result = customerAddressService.createAddress(1L, request);
        assertEquals("London", result.city());
    }

    @Test
    void updateAddress() {
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(customerAddressRepository.save(any(CustomerAddress.class))).thenReturn(customerAddress);
        CustomerAddressCreateRequest request = new CustomerAddressCreateRequest("Gwangju", "Luna", "21", "2");
        CustomerAddressDTO updateAddress = customerAddressService.updateAddress(1L, 1L, request);
        assertEquals("Gwangju", updateAddress.city());
    }

    @Test
    void updateAddress_shouldThrowException_whenAddressBelongsToDifferentCustomer() {
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        CustomerAddressCreateRequest request = new CustomerAddressCreateRequest("London", "Luna", "21", "2");
        assertThrows(CustomerAddressNotFoundException.class, () -> customerAddressService.updateAddress(99L, 1L, request));
    }

    @Test
    void updateAddress_shouldThrowException_whenAddressNotFound() {
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.empty());
        CustomerAddressCreateRequest request = new CustomerAddressCreateRequest("London", "Luna", "21", "2");
        assertThrows(CustomerAddressNotFoundException.class, () -> customerAddressService.updateAddress(customer.getId(), customerAddress.getId(), request));
    }

    @Test
    void getAddressesByCustomerId() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findByCustomerId(1L)).thenReturn(List.of(customerAddress));
        List<CustomerAddressDTO> result = customerAddressService.getAddressesByCustomerId(1L);
        assertEquals(1, result.size());
        assertEquals("London", result.get(0).city());
    }

    @Test
    void deleteAddress() {
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        customerAddressService.deleteAddress(1L, 1L);
        verify(this.customerAddressRepository).delete(customerAddress);
    }

    @Test
    void deleteAddress_shouldThrowException_whenAddressNotFound() {
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CustomerAddressNotFoundException.class, () -> customerAddressService.deleteAddress(1L, 1L));
    }

    @Test
    void deleteAddress_shouldThrowException_whenAddressBelongsToDifferentCustomer() {
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        assertThrows(CustomerAddressNotFoundException.class, () -> customerAddressService.deleteAddress(99L, 1L));
    }
}