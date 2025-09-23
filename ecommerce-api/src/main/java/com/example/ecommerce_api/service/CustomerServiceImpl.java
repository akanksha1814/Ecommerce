package com.example.ecommerce_api.service;

import com.example.ecommerce_api.dto.CustomerDto;
import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    @Override
    public CustomerDto createCustomer(Object request) {
        try {
           CustomerDto customerDto = convertToCustomerDto(request);

            // Check if email already exists
            if (customerRepository.existsByEmail(customerDto.getEmail())) {
                throw new RuntimeException("Customer with email " + customerDto.getEmail() + " already exists");
            }

            Customer customer = Customer.builder()
                    .name(customerDto.getName())
                    .email(customerDto.getEmail())
                    .phone(customerDto.getPhone())
                    .address(customerDto.getAddress())
                    .build();

            Customer savedCustomer = customerRepository.save(customer);
            return convertToDto(savedCustomer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create customer: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return convertToDto(customer);
    }

    @Override
    public CustomerDto updateCustomer(Long id, Object request) {
        try {
            Customer existingCustomer = customerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

            CustomerDto customerDto = convertToCustomerDto(request);

            // Check if email is being changed and if new email already exists
            if (!existingCustomer.getEmail().equals(customerDto.getEmail()) &&
                    customerRepository.existsByEmail(customerDto.getEmail())) {
                throw new RuntimeException("Customer with email " + customerDto.getEmail() + " already exists");
            }

            existingCustomer.setName(customerDto.getName());
            existingCustomer.setEmail(customerDto.getEmail());
            existingCustomer.setPhone(customerDto.getPhone());
            existingCustomer.setAddress(customerDto.getAddress());

            Customer updatedCustomer = customerRepository.save(existingCustomer);
            return convertToDto(updatedCustomer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update customer: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto findCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
        return convertToDto(customer);
    }

    private CustomerDto convertToCustomerDto(Object request) {
        if (request instanceof CustomerDto) {
            return (CustomerDto) request;
        } else if (request instanceof Map) {
            return objectMapper.convertValue(request, CustomerDto.class);
        } else {
            return objectMapper.convertValue(request, CustomerDto.class);
        }
    }

    private CustomerDto convertToDto(Customer customer) {
        return CustomerDto.builder()
                .customer_id(customer.getCustomer_id())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .build();
    }
}