package com.zsy.admin.repository;

import com.zsy.admin.entity.Tag;
import com.zsy.admin.vos.BlogVo;
import com.zsy.admin.vos.TagVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT new com.zsy.admin.vos.TagVo(tag.id,tag.name) from Tag tag")
    List<TagVo> findAllTag();

    @Query("SELECT " +
            "new com.zsy.admin.vos.BlogVo(blog.id,blog.title,blog.description,blog.coverImage,blog.createAt," +
            "blog.category.id,blog.category.name," +
            "blog.user.id,blog.user.nickName) " +
            "FROM Blog blog JOIN blog.tags tag " +
            "WHERE tag.id = :tagId")
    Page<BlogVo> getTagBlogById(Pageable pageable,Integer tagId);
}