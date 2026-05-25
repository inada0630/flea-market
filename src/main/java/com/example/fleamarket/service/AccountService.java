package com.example.fleamarket.service;

import com.example.fleamarket.dto.FleaMarketDTO;
import com.example.fleamarket.entity.FleaMarketEntity;
import com.example.fleamarket.entity.FleaMarketStatus;
import com.example.fleamarket.entity.UserEntity;
import com.example.fleamarket.form.FleaMarketForm;
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

@Service
@RequiredArgsConstructor

public class AccountService {
    private final UserRepository userRepository;
    private final FleaMarketRepository fleaMarketRepository;

    public List<FleaMarketDTO> findAll(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        return fleaMarketRepository.findMyAll(user.id());
    }

    public List<FleaMarketDTO> findSoldAll(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        return fleaMarketRepository.findSoldMyAll(user.id());
    }

    @Transactional
    public void update(Long id,FleaMarketForm form,String username) {
        try {
            UserEntity user = userRepository.findByUsername(username).orElseThrow();
            FleaMarketEntity fleaMarket = fleaMarketRepository.findById(id).orElseThrow();;

            //自身の商品かチェック
            if (fleaMarket.userId() != user.id()){
                throw new RuntimeException("編集権限がありません");
            }
            String imagePath = fleaMarket.imagePath();

            // 新しい画像が選択された場合のみ更新
            if (form.imageFile() != null && !form.imageFile().isEmpty()) {
                MultipartFile file = form.imageFile();
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get("uploads/" + fileName);
                Files.copy(file.getInputStream(), path);
                imagePath = fileName;
            }

            FleaMarketEntity updateEntity = new FleaMarketEntity(
                    id, form.price(), user.id(), form.name(), form.description(),
                    FleaMarketStatus.valueOf(form.status()), imagePath,fleaMarket.buyerId()
            );
            fleaMarketRepository.update(updateEntity);
        } catch (IOException e) {
            throw new RuntimeException("画像保存に失敗しました", e);
        }
    }

    //自身の商品か確認
    public FleaMarketEntity checkOwner(Long fleaMarketId, String username){
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        FleaMarketEntity fleaMarket = fleaMarketRepository.findById(fleaMarketId).orElseThrow();
        if (fleaMarket.userId() != user.id()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return fleaMarket;
    }


    @Transactional
    public void delete(Long fleaMarketId, String username){
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        FleaMarketEntity fleaMarket = fleaMarketRepository.findById(fleaMarketId).orElseThrow();

        // 自分の商品か確認
        if (fleaMarket.userId() != user.id()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // 画像削除
        try {
            Path imagePath = Paths.get("uploads/" + fleaMarket.imagePath());
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fleaMarketRepository.delete(fleaMarketId);
    }

    public List<FleaMarketDTO> findPurchasedItems(String username){
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        return fleaMarketRepository.findPurchasedItems(user.id());
    }
}
