package com.example.mogubackend.repository;


import com.example.mogubackend.entity.Favorite;
import com.example.mogubackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

//    @Query("SELECT f FROM Favorite f JOIN FETCH f.product WHERE f.member.nickname = :userNickname")
//    List<Favorite> findFavoritesWithProductByMemberNickname(String userNickname);

    List<Favorite> findFavoritesWithProductByMemberNicknameOrderByCreatedAtDesc(String userNickname);

    @Query("SELECT f FROM Favorite f JOIN FETCH f.product p WHERE f.member.nickname = :userNickname ORDER BY p.endDate ASC")
    List<Favorite> findFavoritesWithProductByMemberNicknameOrderByEndDateAsc(String userNickname);

    @Query("SELECT f FROM Favorite f JOIN FETCH f.product p WHERE f.member.nickname = :userNickname ORDER BY p.remainingQty DESC")
    List<Favorite> findFavoritesWithProductByMemberNicknameOrderByRemainingQtyDesc(String userNickname);

    boolean existsByMemberNicknameAndProductId(String memberNickname, Long productId);

    Favorite findByMemberIdAndProductId(UUID memberId, Long productId);

    List<Favorite> findByProduct(Product product); //상품 삭제 시 필요
}