package com.delivery.habsida.controller;


import com.delivery.habsida.dto.CustomerCreateRequest;
import com.delivery.habsida.dto.CustomerDTO;
import com.delivery.habsida.dto.CustomerUpdateRequest;
import com.delivery.habsida.repository.CustomerRepository;
import com.delivery.habsida.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody @Valid CustomerCreateRequest customerCreateRequest) {
        CustomerDTO createdCustomerDTO = customerService.createCustomer(customerCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        return customerService.getAllCustomers(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{customerId}")
    public CustomerDTO getCustomerById(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody @Valid CustomerUpdateRequest customerUpdateRequest,
                                                      @PathVariable Long customerId) {
        CustomerDTO updatedCustomerDTO = customerService.updateCustomer(customerId, customerUpdateRequest);
        return ResponseEntity.ok(updatedCustomerDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
