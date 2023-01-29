package com.marketplace.order.orderService;

import com.marketplace.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o from Order o WHERE o.user.id = ?1 and o.isFinished = false")
    Optional<Order> findUnfinishedByUserId(Long userId);
}
