package com.example.mogubackend.controller;

import com.example.mogubackend.dto.ApiResponse;
import com.example.mogubackend.security.UserAuthorize;
import com.example.mogubackend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "멤버 관련 : 로그인 후 사용할 수 있는 API")
@UserAuthorize
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;


    @Operation(summary = "회원 정보 조회")
    @GetMapping
    public ApiResponse getMemberInfo(@AuthenticationPrincipal User user) {
        return ApiResponse.success(memberService.getMemberInfo(UUID.fromString(user.getUsername())));
    }


    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ApiResponse deleteMember(@AuthenticationPrincipal User user) {
        return ApiResponse.success(memberService.deleteMember(UUID.fromString(user.getUsername())));
    }




    @Operation(summary = "회원 닉네임 수정")
    @PutMapping("/update-nickname")
    public ApiResponse updateNickname(@AuthenticationPrincipal User user, @RequestParam String nickname) {
        return ApiResponse.success(memberService.updateNickname(UUID.fromString(user.getUsername()), nickname));
    }



    @Operation(summary = "회원 전화번호 수정")
    @PutMapping("/update-phone")
    public ApiResponse updatePhone(@AuthenticationPrincipal User user, @RequestParam String phone) {
        return ApiResponse.success(memberService.updatePhone(UUID.fromString(user.getUsername()), phone));
    }


    @Operation(summary = "회원 기존 비밀번호 확인")
    @PutMapping("/check-password")
    public ApiResponse checkPassword(@AuthenticationPrincipal User user, @RequestParam String currentPassword) {
        return ApiResponse.success(memberService.checkCurrentPassword(UUID.fromString(user.getUsername()), currentPassword));
    }


    @Operation(summary = "회원 비밀번호 수정")
    @PutMapping("/update-password")
    public ApiResponse updatePassword(@AuthenticationPrincipal User user, @RequestParam String newPassword) {
        return ApiResponse.success(memberService.updatePassword(UUID.fromString(user.getUsername()), newPassword));
    }

}