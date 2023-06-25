package com.zsy.admin.repository;

import com.zsy.admin.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    @Query("SELECT DISTINCT like.ip from Like like")
    Set<String> findAllIp();

    @Query("SELECT like.blogId from Like like")
    String[] findLike(String ip);
}
