package com.delivery.habsida.service;

import com.delivery.habsida.dto.CustomerAddressCreateRequest;
import com.delivery.habsida.dto.CustomerAddressDTO;
import com.delivery.habsida.entity.Customer;
import com.delivery.habsida.entity.CustomerAddress;
import com.delivery.habsida.exception.CustomerAddressNotFoundException;
import com.delivery.habsida.exception.CustomerNotFoundException;
import com.delivery.habsida.repository.CustomerAddressRepository;
import com.delivery.habsida.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerAddressService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;

    public CustomerAddressService(CustomerAddressRepository customerAddressRepository, CustomerRepository customerRepository) {
        this.customerAddressRepository = customerAddressRepository;
        this.customerRepository = customerRepository;
    }

    public CustomerAddressDTO createAddress(Long customerId, CustomerAddressCreateRequest customerAddressCreateRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setCity(customerAddressCreateRequest.city());
        customerAddress.setStreet(customerAddressCreateRequest.street());
        customerAddress.setHouse(customerAddressCreateRequest.house());
        customerAddress.setApartment(customerAddressCreateRequest.apartment());
        customerAddress.setCustomer(customer);
        CustomerAddress savedCustomerAddress = customerAddressRepository.save(customerAddress);
        return CustomerAddressDTO.from(savedCustomerAddress);
    }

    public List<CustomerAddressDTO> getAddressesByCustomerId(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        List<CustomerAddressDTO> list = customerAddressRepository.findByCustomerId(customerId)
                .stream().map(CustomerAddressDTO::from).toList();
        return list;
    }

    public void deleteAddress(Long customerId, Long addressId) {
        CustomerAddress address = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new CustomerAddressNotFoundException("Address not found"));
        if (!address.getCustomer().getId().equals(customerId)) {
            throw new CustomerAddressNotFoundException("Customer address not found");
        }
        customerAddressRepository.delete(address);
    }

    public CustomerAddressDTO updateAddress(Long customerId, Long customerAddressId, CustomerAddressCreateRequest customerAddressCreateRequest) {
        CustomerAddress address = customerAddressRepository.findById(customerAddressId)
                .orElseThrow(() -> new CustomerAddressNotFoundException("Address not found"));
        if (!address.getCustomer().getId().equals(customerId)) {
            throw new CustomerAddressNotFoundException("Customer address not found");
        }

        address.setCity(customerAddressCreateRequest.city());
        address.setStreet(customerAddressCreateRequest.street());
        address.setHouse(customerAddressCreateRequest.house());
        address.setApartment(customerAddressCreateRequest.apartment());

        CustomerAddress updatedCustomerAddress = customerAddressRepository.save(address);
        return CustomerAddressDTO.from(updatedCustomerAddress);
    }

}
