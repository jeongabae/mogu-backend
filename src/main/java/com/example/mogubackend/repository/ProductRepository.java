package com.example.mogubackend.repository;

import com.example.mogubackend.common.DealStatus;
import com.example.mogubackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrderByRemainingQtyDesc(String name, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrderByEndDateAsc(String keyword, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndStatus(String keyword, DealStatus status, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndStatusOrderByRemainingQtyDesc(String keyword, DealStatus status, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndStatusOrderByEndDateAsc(String keyword, DealStatus status, Pageable pageable);

    Page<Product> findByStatus(DealStatus status, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.id = :productId AND p.seller.id = :sellerId")
    boolean existsByProductIdAndSellerId(@Param("productId") Long productId, @Param("sellerId") UUID sellerId);

    Page<Product> findByCategory(String category, Pageable pageable);

    Page<Product> findByLocation(String location, Pageable pageable);

    Page<Product> findByCategoryAndLocation(String category, String location, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.seller.id = :sellerId AND p.status IN ('모집중', '구매진행중') ORDER BY p.createdAt DESC")
    List<Product> findOngoingProductsBySellerIdOrderByCreatedAtDesc(UUID sellerId);

    @Query("SELECT p FROM Product p WHERE p.seller.id = :sellerId AND p.status = '공구종료' ORDER BY p.endDate DESC")
    List<Product> findCompletedProductsBySellerIdOrderByEndDateDesc(UUID sellerId);

    List<Product> findAllByStatusAndEndDateBefore(DealStatus status, LocalDateTime endDate);
}
