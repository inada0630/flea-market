package com.example.fleamarket.repository;

import com.example.fleamarket.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserRepository {
    @Insert("""
            INSERT INTO users(username, password)
            VALUES(#{username}, #{password})
            """)
    void insert(UserEntity entity);

    @Select("""
            SELECT id, username, password
            FROM users
            WHERE username = #{username}
            """)
    Optional<UserEntity> findByUsername(String username);
}
