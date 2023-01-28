package com.marketplace.order.orderService;

import com.marketplace.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT p FROM Order p WHERE p.user.id = ?1")
    List<Order> findAllByUserId(Long id);
}
