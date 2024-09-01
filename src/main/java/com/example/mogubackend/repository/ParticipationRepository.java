package com.example.mogubackend.repository;

import com.example.mogubackend.dto.participation.response.ParticipationResponse;
import com.example.mogubackend.entity.Participation;
import com.example.mogubackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    @Query("SELECT NEW com.example.mogubackend.dto.participation.response.ParticipationResponse(p.member.nickname, p.quantity, p.price, p.member.phone) " +
            "FROM Participation p " +
            "WHERE p.product.id = :productId")
    List<ParticipationResponse> findParticipationByProductId(Long productId);


//    @Query("SELECT p FROM Participation p WHERE p.member.id = :memberId AND p.product.id = :productId")
//    Optional<Participation> findByMemberIdAndProductId(@Param("memberId") UUID memberId, @Param("productId") Long productId);
    Participation findByMemberIdAndProductId(UUID memberId, Long productId);
    @Query("SELECT p.product FROM Participation p WHERE p.member.id = :memberId AND p.product.status IN ('모집중', '구매진행중') ORDER BY p.participateDate DESC")
    List<Product> findOngoingProductsByMemberId(UUID memberId);

    @Query("SELECT p.product FROM Participation p WHERE p.member.id = :memberId AND p.product.status = '공구종료' ORDER BY p.participateDate DESC")
    List<Product> findCompletedProductsByMemberId(UUID memberId);
}