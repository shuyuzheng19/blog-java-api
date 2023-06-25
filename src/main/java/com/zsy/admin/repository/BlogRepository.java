package com.zsy.admin.repository;

import com.zsy.admin.vos.ArchiveBlogVo;
import com.zsy.admin.vos.BlogVo;
import com.zsy.admin.entity.Blog;
import com.zsy.admin.vos.SearchBlog;
import com.zsy.admin.vos.SimpleBlogVo;
import com.zsy.admin.vos.admin.BlogVoA;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Integer>, JpaSpecificationExecutor<Blog> {

    @Query("SELECT " +
            "new com.zsy.admin.vos.BlogVo(blog.id,blog.title,blog.description,blog.coverImage,blog.createAt," +
            "blog.category.id,blog.category.name," +
            "blog.user.id,blog.user.nickName) " +
            "from Blog blog where blog.category is not null")
    Page<BlogVo> paginateAndSortQueries(Pageable pageable);

    @Query("SELECT " +
            "new com.zsy.admin.vos.BlogVo(blog.id,blog.title,blog.description,blog.coverImage,blog.createAt," +
            "blog.category.id,blog.category.name," +
            "blog.user.id,blog.user.nickName) " +
            "from Blog blog where blog.category.id = :cid")
    Page<BlogVo> paginateAndSortQueries(Pageable pageable, Integer cid);

    @Query("SELECT " +
            "new com.zsy.admin.vos.BlogVo(blog.id,blog.title,blog.description,blog.coverImage,blog.createAt," +
            "blog.user.id,blog.user.nickName) " +
            "from Blog blog where blog.category is not null and blog.user.id=:userId")
    Page<BlogVo> findByUserIdBlogs(Pageable pageable, Integer userId);

    @Query("SELECT " +
            "new com.zsy.admin.vos.SearchBlog(blog.id,blog.title,blog.description) " +
            "from Blog blog")
    List<SearchBlog> findAllSearchBlog();

    @Query("SELECT " +
            "new com.zsy.admin.vos.ArchiveBlogVo(blog.id,blog.title,blog.description,blog.createAt) " +
            "from Blog blog where blog.createAt >= :startDate AND blog.createAt <= :endDate")
    Page<ArchiveBlogVo> findBetweenDateBlog(Pageable pageable, Date startDate, Date endDate);

    @Query("SELECT new com.zsy.admin.vos.SimpleBlogVo(blog.id,blog.title,blog.coverImage) from Blog blog where blog.id in :ids")
    List<SimpleBlogVo> findByIdIn(List<Integer> ids);

    @Query("SELECT new com.zsy.admin.vos.SimpleBlogVo(blog.id,blog.title) from Blog blog order by blog.eyeCount desc ")
    List<SimpleBlogVo> selectOrderByEyeCount(Pageable pageable);

    @Query("SELECT new com.zsy.admin.vos.SimpleBlogVo(blog.id,blog.title) from Blog blog")
    List<SimpleBlogVo> findAllSimpleBlog();

    @Query("SELECT new com.zsy.admin.vos.SimpleBlogVo(blog.id,blog.title) from Blog blog where blog.user.id = :userId")
    List<SimpleBlogVo> findUserBlogTop(Pageable pageable,Integer userId);

    @Query(value="UPDATE blogs set eye_count=:eyeCount where id = :id",nativeQuery = true)
    @Modifying
    @Transactional
    void updateEyeCount(Integer id,Integer eyeCount);

    @Query(value="UPDATE blogs set like_count=:likeCount where id =:id",nativeQuery = true)
    @Modifying
    @Transactional
    void updateLikeCount(Integer id,Integer likeCount);

    @Query(value = "update blogs set deleted = true where user_id=:uid and id in(:ids)",nativeQuery = true)
    int deleteBlogByUserId(Integer uid,List<Integer> ids);

    @Query(value = "update blogs set deleted = true where id in(:ids)",nativeQuery = true)
    @Modifying
    @Transactional
    int deleteBlogById(List<Integer> ids);
}
