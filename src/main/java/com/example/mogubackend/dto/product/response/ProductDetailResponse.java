package com.example.mogubackend.dto.product.response;

import com.example.mogubackend.common.DealStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProductDetailResponse(
        @Schema(description = "이미지", example = "1bfc370f-ab3c-42ec-bdb7-8c4a02f8b0d0_IMG_9235.PNG")
        String productImage,
        @Schema(description = "공구 상태", example = "모집중")
        DealStatus dealStatus,
        @Schema(description = "카테고리", example = "디지털기기")
        String category,
        @Schema(description = "구매링크", example = "naver.com")
        String url,
        @Schema(description = "상품명", example = "물")
        String name,
        @Schema(description = "가격", example = "1000")
        int price,
//        @Schema(description = "모집수량", example = "10")
//        int qty,
        @Schema(description = "잔여수량", example = "10")
        int remainingQty,
        @Schema(description = "공구 마감일", example = "2024-06-30T23:59:59")
        LocalDateTime endDate,
        @Schema(description = "설명(공지)", example = "설명~~~~")
        String content,

        @Schema(description = "수령장소", example = "놀이터")
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
