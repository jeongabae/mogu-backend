package com.example.mogubackend.service;

import com.example.mogubackend.dto.like.response.FavoriteResponse;
import com.example.mogubackend.dto.product.response.ProductFavListResponse;
import com.example.mogubackend.dto.product.response.ProductListResponse;
import com.example.mogubackend.entity.Favorite;
import com.example.mogubackend.entity.Member;
import com.example.mogubackend.entity.Product;
import com.example.mogubackend.entity.ProductImage;
import com.example.mogubackend.repository.FavoriteRepository;
import com.example.mogubackend.repository.MemberRepository;
import com.example.mogubackend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Favorite addFav(UUID memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 해당하는 회원을 찾을 수 없습니다: " + memberId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 해당하는 회원을 찾을 수 없습니다: " + productId));
        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setProduct(product);
        favorite.setCreatedAt(LocalDateTime.now());
        return favoriteRepository.save(favorite);
    }



    @Transactional
    public void deleteFav(Long favId) {
        favoriteRepository.deleteById(favId);
    }

    @Transactional
    public List<FavoriteResponse> getFavoritesByMemberNickname(String userNickname) {
        List<Favorite> favorites = favoriteRepository.findFavoritesWithProductByMemberNicknameOrderByCreatedAtDesc(userNickname);

        return favorites.stream().map(favorite -> {
            Product product = favorite.getProduct();
            return new FavoriteResponse(
                    favorite.getId(),
                    new ProductFavListResponse(
                            product.getId(),
                            product.getName(),
                            product.getEndDate(),
                            product.getPrice(),
                            product.getQty(),
                            product.getRemainingQty(),
                            product.getImage().getStoreFileName()
                    )
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<FavoriteResponse> getFavoritesByMemberNicknameOrderByEndDateAsc(String userNickname) {
        List<Favorite> favorites = favoriteRepository.findFavoritesWithProductByMemberNicknameOrderByEndDateAsc(userNickname);

        return favorites.stream().map(favorite -> {
            Product product = favorite.getProduct();
            return new FavoriteResponse(
                    favorite.getId(),
                    new ProductFavListResponse(
                            product.getId(),
                            product.getName(),
                            product.getEndDate(),
                            product.getPrice(),
                            product.getQty(),
                            product.getRemainingQty(),
                            product.getImage().getStoreFileName()
                    )
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<FavoriteResponse> getFavoritesByMemberNicknameOrderByRemainingQtyDesc(String userNickname) {
        List<Favorite> favorites = favoriteRepository.findFavoritesWithProductByMemberNicknameOrderByRemainingQtyDesc(userNickname);

        return favorites.stream().map(favorite -> {
            Product product = favorite.getProduct();
            return new FavoriteResponse(
                    favorite.getId(),
                    new ProductFavListResponse(
                            product.getId(),
                            product.getName(),
                            product.getEndDate(),
                            product.getPrice(),
                            product.getQty(),
                            product.getRemainingQty(),
                            product.getImage().getStoreFileName()
                    )
            );
        }).collect(Collectors.toList());
    }

    public boolean isProductFavoritedByMember(String memberNickname, Long productId) {
        return favoriteRepository.existsByMemberNicknameAndProductId(memberNickname, productId);
    }

    public Long findFavoriteIdByMemberIdAndProductId(UUID userId, Long productId) {
        Favorite favorite = favoriteRepository.findByMemberIdAndProductId(userId, productId);
        return favorite != null ? favorite.getId() : null;
    }

}
