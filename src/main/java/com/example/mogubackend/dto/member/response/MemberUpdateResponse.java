package com.example.mogubackend.dto.member.response;

import com.example.mogubackend.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberUpdateResponse(
        @Schema(description = "회원 정보 수정 성공 여부", example = "true")
        boolean result,
        @Schema(description = "회원 이메일", example = "duck@yonsei.ac.kr")
        String email,
        @Schema(description = "회원 닉네임", example = "노란오리")
        String nickname,
        @Schema(description = "회원 핸드폰번호", example = "010-1234-5678")
        String phone
) {
    public static MemberUpdateResponse of(boolean result, Member member) {
        return new MemberUpdateResponse(result, member.getEmail(), member.getNickname(),member.getPhone());
    }
}
