package com.example.mogubackend.dto.like.response;


import com.example.mogubackend.dto.product.response.ProductFavListResponse;
import com.example.mogubackend.dto.product.response.ProductListResponse;

public record FavoriteResponse(
        Long id,
        ProductFavListResponse product
) {
}
