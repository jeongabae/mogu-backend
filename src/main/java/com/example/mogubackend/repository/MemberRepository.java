package com.example.mogubackend.repository;

import com.example.mogubackend.common.DealStatus;
import com.example.mogubackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByPhone(String phone);

//    @Query("SELECT COUNT(p) FROM Product p WHERE p.seller.id = :sellerId")
//    long countProductsBySellerId(@Param("sellerId") UUID sellerId);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.seller.id = :sellerId AND p.status = :status")
    long countProductsBySellerIdAndStatus(@Param("sellerId") UUID sellerId, @Param("status") DealStatus status);
}
