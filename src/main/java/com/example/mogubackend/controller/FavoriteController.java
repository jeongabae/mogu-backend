package com.example.mogubackend.controller;

import com.example.mogubackend.dto.like.response.FavoriteResponse;
import com.example.mogubackend.entity.Favorite;
import com.example.mogubackend.security.UserAuthorize;
import com.example.mogubackend.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "찜 관련 : 로그인 후 사용할 수 있는 API")
@UserAuthorize
@RequiredArgsConstructor
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "찜 추가")
    @PostMapping("/add")
    @UserAuthorize
    public ResponseEntity<Favorite> addFav(@AuthenticationPrincipal User user, @RequestParam Long productId) {
        Favorite favorite = favoriteService.addFav(UUID.fromString(user.getUsername()), productId);
        return new ResponseEntity<>(favorite, HttpStatus.CREATED);
    }

    @Operation(summary = "찜 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFav(@PathVariable Long id) {
        favoriteService.deleteFav(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "찜 목록 가져오기")
    @GetMapping("/user/{userNickname}")
    public ResponseEntity<List<FavoriteResponse>> getFavoritesByMemberNickname(@PathVariable String userNickname) {
        List<FavoriteResponse> favoriteDTOs = favoriteService.getFavoritesByMemberNickname(userNickname);
        return ResponseEntity.ok(favoriteDTOs);
    }

    @Operation(summary = "마감일 기준(기한 임박 순)으로 정렬된 찜 목록 가져오기")
    @GetMapping("/user/{userNickname}/end-date")
    public ResponseEntity<List<FavoriteResponse>> getFavoritesByMemberNicknameOrderByEndDateAsc(@PathVariable String userNickname) {
        List<FavoriteResponse> favoriteDTOs = favoriteService.getFavoritesByMemberNicknameOrderByEndDateAsc(userNickname);
        return ResponseEntity.ok(favoriteDTOs);
    }

    @Operation(summary = "잔여 수량 기준(추천순)으로 정렬된 찜 목록 가져오기")
    @GetMapping("/user/{userNickname}/recommend")
    public ResponseEntity<List<FavoriteResponse>> getFavoritesByMemberNicknameOrderByRemainingQtyDesc(@PathVariable String userNickname) {
        List<FavoriteResponse> favoriteDTOs = favoriteService.getFavoritesByMemberNicknameOrderByRemainingQtyDesc(userNickname);
        return ResponseEntity.ok(favoriteDTOs);
    }

    @Operation(summary = "상품 유저 찜 확인")
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFavorite(@RequestParam String memberNickname, @RequestParam Long productId) {
        boolean isFavorited = favoriteService.isProductFavoritedByMember(memberNickname, productId);
        return ResponseEntity.ok(isFavorited);
    }

    @Operation(summary = "상품 유저 찜 아이디 조회")
    @GetMapping("/product/{productId}")
    public ResponseEntity<Long> findFavoriteIdByUserIdAndProductId(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        UUID userId = UUID.fromString(user.getUsername());
        Long favoriteId = favoriteService.findFavoriteIdByMemberIdAndProductId(userId, productId);
        if (favoriteId != null) {
            return new ResponseEntity<>(favoriteId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
