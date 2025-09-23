package com.example.ecommerce_api.repository;

import com.example.ecommerce_api.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder_OrderId(Long order_id);

    List<OrderItem> findByProduct_ProductId(Long product_id);

    @Modifying
    @Query("DELETE FROM OrderItem oi WHERE oi.order.order_id = :order_id")
    void deleteByOrder_Id(@Param("order_id") Long order_id);

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.product_id = :product_id")
    Long getTotalQuantitySoldByProduct_Id(@Param("productId") Long productId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.order_id = :order_id AND oi.product.product_id = :product_id")
    List<OrderItem> findByOrder_IdAndProduct_Id(@Param("order_id") Long orderId, @Param("product_id") Long productId);

    @Query("SELECT oi.product.product_id, SUM(oi.quantity) FROM OrderItem oi GROUP BY oi.product.product_id ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopSellingProducts();

}
