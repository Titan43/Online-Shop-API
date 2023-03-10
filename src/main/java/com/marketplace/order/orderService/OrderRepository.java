package com.marketplace.order.orderService;

import com.marketplace.order.orderEntities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o from Order o WHERE o.user.id = ?1")
    List<Order> findAllByUserId(Long userId);

    @Query("SELECT o from Order o WHERE o.user.id = ?1 and o.confirmed = false")
    Optional<Order> findUnfinishedByUserId(Long userId);

    @Query("SELECT o from Order o WHERE o.id = ?1")
    Optional<Order> findByOrderId(Long orderId);
}
