package com.example.mogubackend.dto.verification_code.request;

public record VerificationCodeRequest(
        String email,
        String code
) {
}
