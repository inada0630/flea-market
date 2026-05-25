package com.example.fleamarket.dto;

public record FleaMarketDTO(
        long id,
        long price,
        String username,
        String name,
        String description,
        String status,
        String imagePath
) {
    public String statusLabel() {
        return switch (status) {
            case "ON_SALE" -> "販売中";
            case "SOLD_OUT" -> "売り切れ";
            case "PREPARING" -> "準備中";
            default -> status;
        };
    }
}