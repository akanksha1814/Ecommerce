package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.DTO.CustomerDTO;
import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "APIs for managing customers")
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

    @Operation(summary = "Get all customers", description = "Retrieve a list of all registered customers")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve customer details by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(value -> ResponseEntity.ok(toDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new customer", description = "Add a new customer to the system")
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO dto) {
        Customer saved = customerService.saveCustomer(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    @Operation(summary = "Update customer", description = "Update all details of an existing customer")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO dto) {
        return customerService.getCustomerById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setEmail(dto.getEmail());
                    existing.setPhone(dto.getPhone());
                    existing.setAddress(dto.getAddress());
                    return ResponseEntity.ok(toDTO(customerService.saveCustomer(existing)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Partially update customer", description = "Update some details of an existing customer")
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDTO> patchCustomer(@PathVariable Long id, @RequestBody CustomerDTO dto) {
        return customerService.getCustomerById(id)
                .map(existing -> {
                    if (dto.getName() != null) existing.setName(dto.getName());
                    if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
                    if (dto.getPhone() != null) existing.setPhone(dto.getPhone());
                    if (dto.getAddress() != null) existing.setAddress(dto.getAddress());
                    return ResponseEntity.ok(toDTO(customerService.saveCustomer(existing)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete customer", description = "Delete a customer by their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
