package com.example.fleamarket.dto;

import java.util.List;
import java.util.Optional;

public record FleaMarketSearchDTO(
        String name,
        List<String> statusList,
        Long minPrice,
        Long maxPrice
) {
    public boolean isChecked(String status){
        return Optional.ofNullable(statusList)
                .map(l -> l.contains(status))
                .orElse(false);
    }
}
