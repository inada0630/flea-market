package com.example.fleamarket.form;

import com.example.fleamarket.dto.FleaMarketDTO;
import com.example.fleamarket.entity.FleaMarketEntity;
import com.example.fleamarket.entity.FleaMarketStatus;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public record FleaMarketForm (
        Long id,
        @NotNull(message = "数値を入力してください")
        @Min(1)
        Long price,
        @NotBlank
        @Size(max = 256, message = "256文字以内で入力してください")
        String name,
        @NotBlank
        String description,
        @Pattern(regexp = "ON_SALE|PREPARING",message = "販売中,準備中のいずれかを選択してください")
        String status,
        MultipartFile imageFile
){
    public FleaMarketEntity toEntity(long userId, String imagePath) {

        return new FleaMarketEntity(
                null,
                price,
                userId,
                name,
                description,
                FleaMarketStatus.valueOf(status),
                imagePath,
                null

        );
    }
    public static FleaMarketForm fromEntity(FleaMarketDTO dto) {

        return new FleaMarketForm(
                dto.id(),
                dto.price(),
                dto.name(),
                dto.description(),
                dto.status(),
                null
        );
    }
}
