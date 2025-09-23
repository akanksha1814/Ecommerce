package com.example.ecommerce_api.repository;

import com.example.ecommerce_api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.phone = :phone")
    Optional<Customer> findByPhone(@Param("phone") String phone);

    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:name%")
    List<Customer> findByNameContaining(@Param("name") String name);

    @Query("SELECT c FROM Customer c WHERE c.address LIKE %:address%")
    List<Customer> findByAddressContaining(@Param("address") String address);
}