package com.example.mogubackend.dto.participation.response;

public record ParticipationResponse(
        String memberNickname,
        int quantity,
        int price,
        String memberPhone
) {
}
