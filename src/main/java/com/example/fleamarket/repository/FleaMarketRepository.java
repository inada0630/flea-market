package com.example.fleamarket.repository;


import com.example.fleamarket.dto.FleaMarketDTO;
import com.example.fleamarket.entity.FleaMarketEntity;
import com.example.fleamarket.entity.FleaMarketSearchEntity;
import com.example.fleamarket.entity.FleaMarketStatus;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FleaMarketRepository {

    @Select("""
            <script>
            SELECT f.id, f.price, u.username, f.name, f.description, f.status, f.image_path AS imagePath,buyer_id AS buyerId
            FROM flea_market f
            JOIN users u ON f.user_id = u.id
            WHERE f.user_id != #{userId}
            <if test="condition.name != null and condition.name != ''">
               AND f.name LIKE CONCAT('%', #{condition.name}, '%')
            </if>
            <if test="condition.status != null and !condition.status.isEmpty()">
               AND status IN(
               <foreach item='item' index='index' collection='condition.status' separator=','>
               #{item}
               </foreach>
               )
            </if>
            <if test="condition.minPrice != null">
                <![CDATA[
                AND f.price >= #{condition.minPrice}
                ]]>
            </if>
            
            <if test="condition.maxPrice != null">
                <![CDATA[
                AND f.price <= #{condition.maxPrice}
                ]]>
            </if>
            </script>
            """)
    List<FleaMarketDTO> findAllExcludeUserId(@Param("userId") Long userId,@Param("condition") FleaMarketSearchEntity condition);

    @Select("""
            SELECT f.id, f.price, u.username, f.name, f.description, f.status, f.image_path AS imagePath,buyer_id AS buyerId
            FROM flea_market f
            JOIN users u ON f.user_id = u.id
            WHERE f.id = #{id}
            """)
    Optional<FleaMarketDTO> selectById(@Param("id") long id);

    @Insert("""
            INSERT INTO flea_market(price, user_id, name, description, status, image_path,buyer_id)
            VALUES(#{price}, #{userId}, #{name}, #{description}, #{status}, #{imagePath}, #{buyerId})
            """)
    void insert(FleaMarketEntity entity);

    @Select("""
            SELECT f.id, f.price, u.username, f.name, f.description, f.status, f.image_path AS imagePath,buyer_id AS buyerId
            FROM flea_market f
            JOIN users u ON f.user_id = u.id
            WHERE f.user_id = #{userId}
            AND f.status != 'SOLD_OUT'
            """)
    List<FleaMarketDTO> findMyAll(Long userId);

    @Select("""
            SELECT f.id, f.price, u.username, f.name, f.description, f.status, f.image_path AS imagePath,buyer_id AS buyerId
            FROM flea_market f
            JOIN users u ON f.user_id = u.id
            WHERE f.user_id = #{userId}
            AND f.status = 'SOLD_OUT'
            """)
    List<FleaMarketDTO> findSoldMyAll(Long userId);

    @Select("""
            SELECT id,price,user_id,name,description,status,image_path,buyer_id
            FROM flea_market
            WHERE id = #{id}
            """)
    Optional<FleaMarketEntity> findById(Long id);

    @Update("""
            UPDATE flea_market
            SET price = #{price},name = #{name},description = #{description},status = #{status},image_path = #{imagePath}
            WHERE id = #{id}
            """)
    void update(FleaMarketEntity entity);

    @Delete("""
            DELETE FROM flea_market
            WHERE id = #{id}
            """)
    void delete(Long id);

    @Update("""
            UPDATE flea_market
            SET status = #{status},buyer_id = #{buyerId}
            WHERE id = #{id}
            """)
    void purchase(@Param("id") Long id, @Param("status") FleaMarketStatus fleaMarketStatus, @Param("buyerId") Long buyerId);

    @Select("""
            SELECT f.id,f.price,u.username,f.name,f.description,f.status,f.image_path AS imagePath,f.buyer_id AS buyerId
            FROM flea_market f
            JOIN users u ON f.user_id = u.id
            WHERE f.buyer_id = #{buyerId}
            """)
    List<FleaMarketDTO> findPurchasedItems(Long buyerId);
}





