package com.marketplace.order.orderService;

import com.marketplace.order.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {

    @Query("SELECT op FROM OrderedProduct op WHERE op.order.id = ?1 AND op.product.id = ?2")
    Optional<OrderedProduct> findByOrderIdAndProductId(Long orderId, Long productId);

    @Query("SELECT op FROM OrderedProduct op WHERE op.order.id = ?1")
    List<OrderedProduct> findAllByOrderId(Long orderId);

}
