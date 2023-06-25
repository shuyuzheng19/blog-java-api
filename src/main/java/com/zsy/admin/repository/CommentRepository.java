package com.zsy.admin.repository;

import com.zsy.admin.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT comment from Comment comment where comment.parentId is null and comment.blogId=:blogId order by comment.likes desc,comment.createTime desc")
    Page<Comment> findCommentByBlogId(Pageable pageable,Integer blogId);
}
