package com.example.ecommerce_api.controller;



import com.example.ecommerce_api.dto.ApiResponse;
import com.example.ecommerce_api.dto.CustomerDto;
import com.example.ecommerce_api.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
//@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create a new customer", description = "Create a new customer with provided details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(@Valid @RequestBody Object request) {
        try {
            CustomerDto customerDto = customerService.createCustomer(request);
            ApiResponse<CustomerDto> response = ApiResponse.<CustomerDto>builder()
                    .success(true)
                    .message("Customer created successfully")
                    .data(customerDto)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<CustomerDto> response = ApiResponse.<CustomerDto>builder()
                    .success(false)
                    .message("Failed to create customer")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getAllCustomers() {
        try {
            List<CustomerDto> customers = customerService.getAllCustomers();
            ApiResponse<List<CustomerDto>> response = ApiResponse.<List<CustomerDto>>builder()
                    .success(true)
                    .message("Customers retrieved successfully")
                    .data(customers)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<CustomerDto>> response = ApiResponse.<List<CustomerDto>>builder()
                    .success(false)
                    .message("Failed to retrieve customers")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by their ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer found successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerById(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        try {
            CustomerDto customer = customerService.getCustomerById(id);
            ApiResponse<CustomerDto> response = ApiResponse.<CustomerDto>builder()
                    .success(true)
                    .message("Customer found successfully")
                    .data(customer)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<CustomerDto> response = ApiResponse.<CustomerDto>builder()
                    .success(false)
                    .message("Customer not found")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Update customer", description = "Update an existing customer's information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Valid @RequestBody Object request) {
        try {
            CustomerDto updatedCustomer = customerService.updateCustomer(id, request);
            ApiResponse<CustomerDto> response = ApiResponse.<CustomerDto>builder()
                    .success(true)
                    .message("Customer updated successfully")
                    .data(updatedCustomer)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<CustomerDto> response = ApiResponse.<CustomerDto>builder()
                    .success(false)
                    .message("Failed to update customer")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Delete customer", description = "Delete a customer by their ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("Customer deleted successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(false)
                    .message("Failed to delete customer")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Search customers by email", description = "Find customer by email address")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer search completed")
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<CustomerDto>> searchCustomerByEmail(
            @Parameter(description = "Email address") @RequestParam String email) {
        try {
            CustomerDto customer = customerService.findCustomerByEmail(email);
            ApiResponse<CustomerDto> response = ApiResponse.<CustomerDto>builder()
                    .success(true)
                    .message("Customer found successfully")
                    .data(customer)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<CustomerDto> response = ApiResponse.<CustomerDto>builder()
                    .success(false)
                    .message("Customer not found with provided email")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
