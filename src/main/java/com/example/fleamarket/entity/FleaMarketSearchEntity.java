package com.example.fleamarket.entity;

import java.util.List;
public record FleaMarketSearchEntity(
        String name,
        List<FleaMarketStatus> status,
        Long minPrice,
        Long maxPrice
) {
}
