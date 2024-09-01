package com.example.mogubackend.dto.product.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;

public record ProductRegisterRequest(
        @Schema(description = "상품명", example = "아이폰14")
        String name,

        @Schema(description = "구매링크", example = "naver.com")
        String url,

        @Schema(description = "상품 이미지")
        MultipartFile image,

        @Schema(description = "카테고리", example = "디지털기기")
        String category,

        @Schema(description = "개당 가격", example = "10000000")
        int price,

        @Schema(description = "모집 수량", example = "10")
        int qty,

        @Schema(description = "모집자가 가져갈 개수", example = "10")
        int participate_qty,

        @Schema(description = "공구 마감일", example = "2024-06-30T23:59:59")
        LocalDateTime endDate,

        @Schema(description = "최소 수량", example = "5")
        int mqq,

        @Schema(description = "설명(공지)", example = "설명~~~~")
        String content,

        @Schema(description = "수령장소 카테고리", example = "놀이터")
        String location,

        @Schema(description = "위도", example = "37.2770")
        double latitude, //위도

        @Schema(description = "경도", example = "127.9025")
        double longitude, //경도

        @Schema(description = "수령장소 설명", example = "gs25 앞")
        String locationName, //수령 장소 설명

        @Schema(description = "오픈채팅방링크", example = "http://kakakopenchat.hello")
        String chatUrl
    ) {
}
