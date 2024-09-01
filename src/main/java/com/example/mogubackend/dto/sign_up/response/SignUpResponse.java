package com.example.mogubackend.dto.sign_up.response;

import com.example.mogubackend.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record SignUpResponse(
        @Schema(description = "회원 고유키", example = "a0a00520-1abb-5b2b-8b0a-5b2c020f2e0a")
        UUID id,
//        @Schema(description = "회원 아이디", example = "duck")
//        String account,

        @Schema(description = "회원 이메일", example = "duck@naver.com")
        String email,

        @Schema(description = "회원 닉네임", example = "노란오리")
        String nickname,

        @Schema(description = "회원 핸드폰번호", example = "010-1234-5678")
        String phone

) {
    public static SignUpResponse from(Member member) {
        return new SignUpResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getPhone()
        );
    }
}
