package com.example.fleamarket.service;

import com.example.fleamarket.entity.UserEntity;
import com.example.fleamarket.form.UserForm;
import com.example.fleamarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserForm form) {
        // パスワード暗号化
        String encodedPassword =
                passwordEncoder.encode(form.password());
        UserEntity entity = new UserEntity(null, form.username(), encodedPassword);
        userRepository.insert(entity);
    }

    //入力された情報を取得してDB検索 | 見つからなかった場合はログイン失敗
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));

        return User.builder().username(user.username()).password(user.password()).build();
    }
}
