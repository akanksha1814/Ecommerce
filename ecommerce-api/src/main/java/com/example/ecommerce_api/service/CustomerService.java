package com.example.ecommerce_api.service;

import com.example.ecommerce_api.dto.CustomerDto;
import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(Object request);
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(Long id);
    CustomerDto updateCustomer(Long id, Object request);
    void deleteCustomer(Long id);
    CustomerDto findCustomerByEmail(String email);
}
