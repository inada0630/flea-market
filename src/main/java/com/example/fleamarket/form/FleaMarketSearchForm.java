package com.example.fleamarket.form;

import com.example.fleamarket.dto.FleaMarketSearchDTO;
import com.example.fleamarket.entity.FleaMarketSearchEntity;
import com.example.fleamarket.entity.FleaMarketStatus;
import java.util.List;
import java.util.Optional;

public record FleaMarketSearchForm(
        String name,
        List<String> status,
        Long minPrice,
        Long maxPrice
) {
    public FleaMarketSearchEntity toEntity(){
        //statusがnullの場合
        var statusEntityList = Optional.ofNullable(status())
                .map(statusList -> statusList.stream().map(FleaMarketStatus::valueOf).toList())
                .orElse(List.of());

        return new FleaMarketSearchEntity(name(),statusEntityList,minPrice(),maxPrice());
    }

    public FleaMarketSearchDTO toDTO() {
        return new FleaMarketSearchDTO(name(),status(),minPrice(),maxPrice());
    }
}
