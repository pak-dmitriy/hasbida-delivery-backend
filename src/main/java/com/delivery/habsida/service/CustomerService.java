package com.delivery.habsida.service;

import com.delivery.habsida.dto.CustomerCreateRequest;
import com.delivery.habsida.dto.CustomerDTO;
import com.delivery.habsida.dto.CustomerUpdateRequest;
import com.delivery.habsida.entity.Customer;
import com.delivery.habsida.entity.CustomerStatus;
import com.delivery.habsida.exception.CustomerNotFoundException;
import com.delivery.habsida.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO createCustomer(CustomerCreateRequest customerCreateRequest) {
        Customer customer = new Customer();
        customer.setName(customerCreateRequest.name());
        customer.setPhone(customerCreateRequest.phone());
        customer.setStatus(CustomerStatus.ACTIVE);
        Customer savedCustomer = customerRepository.save(customer);

        return CustomerDTO.from(savedCustomer);

    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerDTO::from).toList();
    }

    public CustomerDTO updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customer.setName(customerUpdateRequest.name());
        customer.setPhone(customerUpdateRequest.phone());
        customer.setStatus(customerUpdateRequest.status());
        Customer updatedCustomer = customerRepository.save(customer);
        return CustomerDTO.from(updatedCustomer);
    }

    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    public CustomerDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return CustomerDTO.from(customer);
    }
}
