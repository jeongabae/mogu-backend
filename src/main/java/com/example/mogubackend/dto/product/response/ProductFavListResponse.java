package com.example.mogubackend.dto.product.response;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ProductFavListResponse(
        @Schema(description = "상품아이디", example = "1")
        Long id,
        @Schema(description = "상품명", example = "물")
        String name,
//        @Schema(description = "카테고리", example = "물음료")
//        String category,
        @Schema(description = "공구 마감일", example = "2024-06-30T23:59:59")
        LocalDateTime endDate,
        @Schema(description = "가격", example = "1000")
        int price,
        @Schema(description = "모집수량", example = "10")
        int qty,
        @Schema(description = "잔여수량", example = "10")
        int remainingQty,
        @Schema(description = "이미지", example = "1bfc370f-ab3c-42ec-bdb7-8c4a02f8b0d0_IMG_9235.PNG")
        String productImage
//
) {
}


