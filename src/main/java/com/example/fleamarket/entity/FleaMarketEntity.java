package com.example.fleamarket.entity;

public record FleaMarketEntity(
        Long id,
        long price,
        long userId,
        String name,
        String description,
        FleaMarketStatus status,
        String imagePath,
        Long buyerId
) {
}
