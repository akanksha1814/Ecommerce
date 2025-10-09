package com.example.ecommerce_api.dto;

import java.util.Objects;

public class CustomerUpdateDTO {

    private String name;
    private String phone;
    private String address;

    // 1. No-Argument Constructor
    public CustomerUpdateDTO() {
    }

    // 2. All-Argument Constructor
    public CustomerUpdateDTO(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // 3. Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // 4. (Optional but recommended) equals(), hashCode(), and toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerUpdateDTO that = (CustomerUpdateDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(phone, that.phone) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, address);
    }

    @Override
    public String toString() {
        return "CustomerUpdateDTO{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}