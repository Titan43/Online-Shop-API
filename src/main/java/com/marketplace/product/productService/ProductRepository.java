package com.marketplace.product.productService;

import com.marketplace.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.user.id = ?1")
    List<Product> findAllByUserId(Long id);

    @Query("SELECT p FROM Product p WHERE p.isAvailable = true")
    Page<Product> findAllAvailable(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isAvailable = true AND p.id = ?1")
    Optional<Product> findByIdAvailable(Long id);
}
