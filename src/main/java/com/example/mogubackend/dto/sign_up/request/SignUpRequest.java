package com.example.mogubackend.dto.sign_up.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignUpRequest(

        @Schema(description = "회원 이메일", example = "duck@naver.com")
        String email,

        @Schema(description = "회원 닉네임", example = "오리")
        String nickname,

        @Schema(description = "회원 핸드폰번호", example = "010-1234-5678")
        String phone,
        @Schema(description = "회원 비밀번호", example = "1234")
        String password
//        @Schema(description = "비밀번호 확인", example = "1234")
//        String password2,


) {
}
