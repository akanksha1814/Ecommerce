package com.example.ecommerce_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private Long customer_id;


    private String name;
    @Email(message = "Email should be valid")
    private String email;


    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone must be 10-15 digits")
    private String phone;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;
}
