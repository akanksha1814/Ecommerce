package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.repository.CustomerRepository;
import com.example.ecommerce_api.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @Transactional
    public Customer findOrCreateByEmail(String email) {

        String normalizedEmail = email.toLowerCase();

        return customerRepository.findByEmail(normalizedEmail)
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(normalizedEmail);
                    return customerRepository.save(newCustomer);
                });
    }
    public Customer updateCustomer(Long id, Map<String, Object> updates) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    customer.setName((String) value);
                    break;
                case "phone":
                    customer.setPhone((String) value);
                    break;
                case "address":
                    customer.setAddress((String) value);
                    break;
            }
        });
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        // CascadeType.ALL on Customer entity will handle deleting orders
        customerRepository.deleteById(id);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }
}