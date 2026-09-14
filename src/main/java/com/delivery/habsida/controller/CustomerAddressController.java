package com.delivery.habsida.controller;

import com.delivery.habsida.dto.CustomerAddressCreateRequest;
import com.delivery.habsida.dto.CustomerAddressDTO;
import com.delivery.habsida.service.CustomerAddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customers/{customerId}/addresses")
public class CustomerAddressController {
    private final CustomerAddressService customerAddressService;

    public CustomerAddressController(CustomerAddressService customerAddressService) {
        this.customerAddressService = customerAddressService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomerAddressDTO> createAddress(@PathVariable Long customerId,
                                                            @RequestBody @Valid CustomerAddressCreateRequest request) {
        CustomerAddressDTO customerAddressDTO = customerAddressService.createAddress(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerAddressDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<CustomerAddressDTO> getAddress(@PathVariable Long customerId) {
        return customerAddressService.getAddressesByCustomerId(customerId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{addressId}")
    public ResponseEntity<CustomerAddressDTO> updateAddress(@RequestBody @Valid CustomerAddressCreateRequest customerAddressCreateRequest,
                                                            @PathVariable Long customerId,
                                                            @PathVariable Long addressId) {
        CustomerAddressDTO updateAddress = customerAddressService.updateAddress(customerId, addressId, customerAddressCreateRequest);
        return ResponseEntity.ok(updateAddress);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long customerId, @PathVariable Long addressId) {
        customerAddressService.deleteAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }
}
