package com.example.ecommerce_api.dto;

public class GenericResponse {

    private String message;

    // 1. A no-argument constructor (good practice)
    public GenericResponse() {
    }

    // 2. The constructor that takes a String (this will fix your error)
    public GenericResponse(String message) {
        this.message = message;
    }

    // 3. Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}