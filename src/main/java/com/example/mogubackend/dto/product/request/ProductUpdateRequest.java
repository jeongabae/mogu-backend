package com.example.mogubackend.dto.product.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProductUpdateRequest(
        @Schema(description = "구매링크", example = "naver.com")
        String url,

        @Schema(description = "상품 이미지")
        MultipartFile image,

        @Schema(description = "설명(공지)", example = "설명~~~~")
        String content,

        @Schema(description = "오픈채팅방링크", example = "http://kakakopenchat.hello")
        String chatUrl
) {}

