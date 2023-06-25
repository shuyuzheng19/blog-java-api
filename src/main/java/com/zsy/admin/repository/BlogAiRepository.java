package com.zsy.admin.repository;

import com.zsy.admin.entity.BlogAi;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BlogAiRepository extends JpaRepository<BlogAi, Integer> {

    @Query(nativeQuery = true,value = "update blogs set blog_ai_id=:aid where id=:bid")
    @Modifying
    @Transactional
    void updateAi(Integer aid,Integer bid);
}
