package com.example.mogubackend.dto.member.response;

import com.example.mogubackend.common.MemberType;
import com.example.mogubackend.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record MemberInfoResponse(
        @Schema(description = "회원 고유키", example = "c0a00520-7abc-5b5b-8b0a-6b1c032f0e4a")
        UUID id,
        @Schema(description = "회원 이메일", example = "duck@yonsei.ac.kr")
        String email,
        @Schema(description = "회원 닉네임", example = "노란오리")
        String nickname,
        @Schema(description = "회원 핸드폰번호", example = "010-1234-5678")
        String phone,
        @Schema(description = "회원 타입", example = "USER")
        MemberType type,
        @Schema(description = "회원 생성일", example = "2024-03-27T15:00:00")
        LocalDateTime createdAt
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getPhone(),
                member.getType(),
                member.getCreatedAt()
        );
    }
}
