package com.example.fleamarket.service;

import com.example.fleamarket.dto.FleaMarketDTO;
import com.example.fleamarket.entity.FleaMarketEntity;
import com.example.fleamarket.entity.FleaMarketSearchEntity;
import com.example.fleamarket.entity.FleaMarketStatus;
import com.example.fleamarket.entity.UserEntity;
import com.example.fleamarket.form.FleaMarketForm;
import com.example.fleamarket.form.FleaMarketSearchForm;
import com.example.fleamarket.repository.FleaMarketRepository;
import com.example.fleamarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class FleaMarketService {

    private final FleaMarketRepository fleaMarketRepository;
    private final UserRepository userRepository;

    public List<FleaMarketDTO> findAll(String username, FleaMarketSearchEntity searchEntity) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        return fleaMarketRepository.findAllExcludeUserId(user.id(),searchEntity);
    }

    public FleaMarketDTO findById(long id) {
    return fleaMarketRepository.selectById(id)
            .orElseThrow(() -> new RuntimeException("商品が見つかりません"));
    }


    @Transactional
    public void create(FleaMarketForm form,String username){
        try {
            // アップロードされた画像情報を取得し、/uploadとDBに保存
            MultipartFile file = form.imageFile();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            Files.copy(file.getInputStream(), path);

            UserEntity user = userRepository.findByUsername(username).orElseThrow();
            Long userId = user.id();

            FleaMarketEntity entity = form.toEntity(userId, fileName);
            fleaMarketRepository.insert(entity);
        } catch (IOException e) {
            throw new RuntimeException("画像保存に失敗しました", e);
        }
    }

    @Transactional
    public void purchase(Long fleaMarketId, String username){
        UserEntity buyer = userRepository.findByUsername(username).orElseThrow();
        FleaMarketEntity fleaMarket = fleaMarketRepository.findById(fleaMarketId).orElseThrow();
        // 自分の商品は買えない
        if (fleaMarket.userId() == buyer.id()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // 売り切れ確認
        if (fleaMarket.status() == FleaMarketStatus.SOLD_OUT) {
            throw new IllegalStateException("売り切れです");
        }
        fleaMarketRepository.purchase(fleaMarketId, FleaMarketStatus.SOLD_OUT, buyer.id()
        );
    }
}
