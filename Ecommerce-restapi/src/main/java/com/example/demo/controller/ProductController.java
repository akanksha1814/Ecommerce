package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @PostMapping
    public String createProduct(@AuthenticationPrincipal Jwt jwt, @RequestBody String productData) {
        // Spring ne already JWT validate kar diya hoga
        String email = jwt.getClaim("email");
        return "✅ Product created by: " + email + " | data: " + productData;
    }

    @GetMapping
    public String getProducts(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        return "Products fetched by: " + email;
    }
}
