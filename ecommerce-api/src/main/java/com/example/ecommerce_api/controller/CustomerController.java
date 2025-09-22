package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.DTO.CustomerDTO;
import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Helper: map Entity -> DTO
    private CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .build();
    }

    // Helper: map DTO -> Entity
    private Customer toEntity(CustomerDTO dto) {
        return Customer.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(value -> ResponseEntity.ok(toDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCustomerPartially(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        return customerService.getCustomerById(id)
                .map(existingCustomer -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "name":
                                existingCustomer.setName((String) value);
                                break;
                            case "phone":
                                existingCustomer.setPhone((String) value);
                                break;
                            case "address":
                                existingCustomer.setAddress((String) value);
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid field: " + key);
                        }
                    });
                    Customer updatedCustomer = customerService.saveCustomer(existingCustomer);
                    return ResponseEntity.ok(updatedCustomer);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Map<String, String>> deleteCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        Map<String, String> response = new HashMap<>();

        if (customer.isPresent()) {
            customerService.deleteCustomer(id);
            response.put("message", "Customer with ID " + id + " was successfully deleted.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Customer with ID " + id + " was not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
