package com.delivery.habsida.service;

import com.delivery.habsida.dto.CustomerCreateRequest;
import com.delivery.habsida.dto.CustomerDTO;
import com.delivery.habsida.dto.CustomerUpdateRequest;
import com.delivery.habsida.entity.Customer;
import com.delivery.habsida.entity.CustomerStatus;
import com.delivery.habsida.exception.CustomerNotFoundException;
import com.delivery.habsida.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
       customer = new Customer();
       customer.setId(1L);
       customer.setName("John");
       customer.setPhone("123456789");
       customer.setStatus(CustomerStatus.ACTIVE);
    }


    @Test
    void createCustomer() {
        CustomerCreateRequest request = new CustomerCreateRequest("John", "123456789");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerDTO result = customerService.createCustomer(request);
        assertEquals("John", result.name());
    }

    @Test
    void getAllCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        when(customerRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(customer)));
        Page<CustomerDTO> result = customerService.getAllCustomers(pageable);
        assertEquals(1, result.getContent().size());
        assertEquals("John", result.getContent().get(0).name());

    }

    @Test
    void updateCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerUpdateRequest request = new CustomerUpdateRequest("Dima", "222222", CustomerStatus.BLOCKED);
        CustomerDTO updateCustomer = customerService.updateCustomer(1L, request);
        assertEquals("Dima", updateCustomer.name());
    }

    @Test
    void deleteCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        customerService.deleteCustomer(1L);
        verify(this.customerRepository).delete(customer);
    }

    @Test
    void getCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        CustomerDTO result = customerService.getCustomerById(1L);
        assertEquals("John", result.name());
    }

    @Test
    void getCustomerById_notFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(99L));
    }
}