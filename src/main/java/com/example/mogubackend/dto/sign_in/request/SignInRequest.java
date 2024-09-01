package com.example.mogubackend.dto.sign_in.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInRequest(
        @Schema(description = "회원 이메일", example = "duck@naver.com")
        String email,
        @Schema(description = "회원 비밀번호", example = "1234")
        String password
) {
}
