package com.example.mogubackend.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberUpdateRequest(
        @Schema(description = "회원 비밀번호", example = "1234")
        String password,
        @Schema(description = "회원 새 비밀번호", example = "1234")
        String newPassword,
        @Schema(description = "회원 이름", example = "노란오리")
        String nickname,
//        @Schema(description = "회원 이메일", example = "duck@naver.com")
//        String email,
        @Schema(description = "회원 핸드폰번호", example = "010-1234-5678")
        String phone

) {
}
