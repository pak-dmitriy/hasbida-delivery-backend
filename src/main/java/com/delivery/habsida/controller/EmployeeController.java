package com.delivery.habsida.controller;

import com.delivery.habsida.dto.EmployeeCreateRequest;
import com.delivery.habsida.dto.EmployeeCreateResponse;
import com.delivery.habsida.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeCreateResponse> createEmployee(@RequestBody @Valid EmployeeCreateRequest request) {
        EmployeeCreateResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{userId}/stores/{storeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignStoreToEmployee(
            @PathVariable Long userId,
            @PathVariable Long storeId) {
        employeeService.assignEmployeeToStore(userId, storeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
