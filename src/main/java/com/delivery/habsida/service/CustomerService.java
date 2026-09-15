package com.delivery.habsida.service;

import com.delivery.habsida.dto.CustomerCreateRequest;
import com.delivery.habsida.dto.CustomerDTO;
import com.delivery.habsida.dto.CustomerUpdateRequest;
import com.delivery.habsida.entity.Customer;
import com.delivery.habsida.entity.CustomerStatus;
import com.delivery.habsida.exception.CustomerAddressNotFoundException;
import com.delivery.habsida.exception.CustomerAlreadyExistsException;
import com.delivery.habsida.exception.CustomerNotFoundException;
import com.delivery.habsida.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO createCustomer(CustomerCreateRequest customerCreateRequest) {
        String phone = customerCreateRequest.phone();
        Optional<Customer> existCustomer = customerRepository.findByPhone(phone);
        if (existCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer with phone " + phone + " already exists");
        }
        Customer customer = new Customer();
        customer.setName(customerCreateRequest.name());
        customer.setPhone(phone);
        customer.setStatus(CustomerStatus.ACTIVE);
        Customer savedCustomer = customerRepository.save(customer);

        return CustomerDTO.from(savedCustomer);

    }

    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(CustomerDTO::from);
    }

    public CustomerDTO updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customer.setName(customerUpdateRequest.name());
        String phone = customerUpdateRequest.phone();
        Optional<Customer> existCustomer = customerRepository.findByPhone(phone);
        if (existCustomer.isPresent() && !existCustomer.get().getId().equals(customerId)) {
            throw new CustomerAlreadyExistsException("Customer with phone " + phone + " already exists");
        }
        customer.setPhone(phone);
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
