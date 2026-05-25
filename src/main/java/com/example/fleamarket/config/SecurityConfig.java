package com.example.fleamarket.config;

import com.example.fleamarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // ログイン不要
                        .requestMatchers(
                                "/register",
                                "/login",
                                "/css/**",
                                "/uploads/**",
                                "/webjars/**"
                        ).permitAll()

                        // それ以外はログイン必要
                        .anyRequest().authenticated()
                )

                // ログイン設定
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/shopping")
                        .permitAll()
                )

                // UserServiceを使用
                .userDetailsService(userService);

        return http.build();
    }


}