package com.example.mogubackend.dto.product.response;

import com.example.mogubackend.common.DealStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProductListByBuyerResponse(
        @Schema(description = "상품아이디", example = "1")
        Long id,
        @Schema(description = "상품명", example = "물")
        String name,

        @Schema(description = "거래 상태", example = "모집중")
        DealStatus status,

        @Schema(description = "잔여수량", example = "10")
        int remainingQty,

        @Schema(description = "모집수량", example = "10")
        int qty,

        @Schema(description = "내가 참여한 개수", example = "1")
        int participate_qty,

        @Schema(description = "내가 낼 가격", example = "1000")
        int participate_price,

        @Schema(description = "수령장소 카테고리", example = "놀이터")
        String location,

        @Schema(description = "이미지", example = "1bfc370f-ab3c-42ec-bdb7-8c4a02f8b0d0_IMG_9235.PNG")
        String productImage
) {
}
