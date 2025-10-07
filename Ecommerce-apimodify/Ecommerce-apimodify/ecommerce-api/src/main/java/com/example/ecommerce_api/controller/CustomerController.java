package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.dto.GenericResponse;
import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get a customer by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Operation(summary = "Get all customers")
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @Operation(summary = "Update a customer's details partially")
    @PatchMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,  @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(customerService.updateCustomer(id, updates));
    }

    @Operation(summary = "Delete a customer and all their orders")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(new GenericResponse("Customer with ID " + id + " and all their orders deleted successfully."));
    }
}