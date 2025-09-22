package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/success")
    public ResponseEntity<Map<String, Object>> authSuccess(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> response = new HashMap<>();
        
        if (principal != null) {
            response.put("message", "Authentication successful");
            response.put("user", Map.of(
                "name", principal.getAttribute("name"),
                "email", principal.getAttribute("email"),
                "picture", principal.getAttribute("picture")
            ));
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Authentication failed");
            response.put("status", "error");
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/failure")
    public ResponseEntity<Map<String, String>> authFailure() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Authentication failed");
        response.put("status", "error");
        return ResponseEntity.status(401).body(response);
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            Map<String, Object> user = new HashMap<>();
            user.put("name", principal.getAttribute("name"));
            user.put("email", principal.getAttribute("email"));
            user.put("picture", principal.getAttribute("picture"));
            user.put("authorities", principal.getAuthorities());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        response.put("redirect", "/");
        return ResponseEntity.ok(response);
    }
}