package com.example.mogubackend.controller;

import com.example.mogubackend.dto.participation.response.ParticipationResponse;
import com.example.mogubackend.dto.product.response.ProductListByBuyerResponse;
import com.example.mogubackend.entity.Participation;
import com.example.mogubackend.security.UserAuthorize;
import com.example.mogubackend.service.ParticipationService;
import com.example.mogubackend.dto.ApiResponse;
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

@Tag(name = "공구 참여 관련 : 로그인 후 사용할 수 있는 API")
@UserAuthorize
@RequiredArgsConstructor
@RestController
@RequestMapping("/participation")
public class ParticipationController {

    private final ParticipationService participationService;

    @Operation(summary = "공구 참여")
    @PostMapping
    public ResponseEntity<ApiResponse> participateInProduct(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @AuthenticationPrincipal User user) {

        UUID memberId = UUID.fromString(user.getUsername());
        Participation participation = participationService.participateInProduct(productId, quantity, memberId);
        return ResponseEntity.ok(ApiResponse.success(participation));
    }

    @Operation(summary = "특정 상품의 공구 참여자 정보 조회")
    @GetMapping("/{productId}")
    public ResponseEntity<List<ParticipationResponse>> getParticipationByProductId(@PathVariable Long productId) {
        List<ParticipationResponse> participationList = participationService.getParticipationByProductId(productId);
        return ResponseEntity.ok(participationList);
    }

//    @Operation(summary = "현재 로그인된 유저 UUID로 해당 공구에 참여 여부 조회 : 로그인 유저 토큰 필요")
//    @GetMapping("/check")
//    public ResponseEntity<?> checkParticipation(
//            @AuthenticationPrincipal User user,
//            @RequestParam Long productId) {
//        UUID memberId = UUID.fromString(user.getUsername());
//
//        boolean isParticipating = participationService.isUserParticipating(memberId, productId);
//
//        if (isParticipating) {
//            return ResponseEntity.ok("Y");
//        } else {
//            return ResponseEntity.ok("N");
//        }
//    }

    @Operation(summary = "현재 로그인된 유저 UUID로 해당 공구에 참여 여부 조회: 로그인 유저 토큰 필요(참여 'Y', 취소 가능 'Cancellable', 참여X 'N'")
    @GetMapping("/check")
    public ResponseEntity<String> checkParticipation(
            @AuthenticationPrincipal User user,
            @RequestParam Long productId) {

        UUID memberId = UUID.fromString(user.getUsername());
        String participationStatus = participationService.checkParticipationStatus(memberId, productId);

        return ResponseEntity.ok(participationStatus);
    }


    @Operation(summary = "내가 참여중인 공구 목록 가져오기: 로그인 유저 토큰 필요")
    @GetMapping("/ongoing")
    public ResponseEntity<List<ProductListByBuyerResponse>> getOngoingProductsByMemberId(@AuthenticationPrincipal User user) {
        UUID memberId = UUID.fromString(user.getUsername());
        List<ProductListByBuyerResponse> products = participationService.getOngoingProductsByMemberId(memberId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "내가 참여한 공구 목록 가져오기: 로그인 유저 토큰 필요")
    @GetMapping("/completed")
    public ResponseEntity<List<ProductListByBuyerResponse>> getCompletedProductsByMemberId(@AuthenticationPrincipal User user) {
        UUID memberId = UUID.fromString(user.getUsername());
        List<ProductListByBuyerResponse> products = participationService.getCompletedProductsByMemberId(memberId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "현재 로그인된 유저 UUID로 해당 공구 참여 삭제: 로그인 유저 토큰 필요")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteParticipation(
            @AuthenticationPrincipal User user,
            @RequestParam Long productId) {

        UUID memberId = UUID.fromString(user.getUsername());
        boolean isDeleted = participationService.deleteParticipation(memberId, productId);

        if (isDeleted) {
            return ResponseEntity.ok("Participation deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Participation not found or cannot be deleted.");
        }
    }
}
