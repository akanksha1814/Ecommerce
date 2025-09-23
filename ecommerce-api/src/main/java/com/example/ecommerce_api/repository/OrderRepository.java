package com.example.ecommerce_api.repository;

import com.example.ecommerce_api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer_CustomerId(Long customer_id);

    List<Order> findByOrderStatus(String orderStatus);

    @Query("SELECT o FROM Order o WHERE o.totalAmount >= :minAmount")
    List<Order> findOrdersAboveAmount(@Param("minAmount") Double minAmount);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT o FROM Order o WHERE o.customer.customer_id = :customer_id AND o.orderStatus = :status")
    List<Order> findByCustomerIdAndStatus(@Param("customer_id") Long customer_id, @Param("status") String status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.customer_id = :customer_id")
    Long countOrdersByCustomerId(@Param("customer_id") Long customer_id);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.customer.customer_id = :customer_id")
    Double getTotalAmountByCustomerId(@Param("customer_id") Long customer_id);
}