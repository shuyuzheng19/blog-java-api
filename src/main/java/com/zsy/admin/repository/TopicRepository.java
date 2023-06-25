package com.zsy.admin.repository;

import com.zsy.admin.entity.Topic;
import com.zsy.admin.vos.BlogVo;
import com.zsy.admin.vos.SimpleBlogVo;
import com.zsy.admin.vos.SimpleTopicVo;
import com.zsy.admin.vos.TopicVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query("SELECT new com.zsy.admin.vos.SimpleTopicVo(topic.id,topic.name) from Topic topic where topic.user.id=:userId")
    List<SimpleTopicVo> findSimpleTopicVoList(Integer userId);


    @Query("SELECT new com.zsy.admin.vos.TopicVo(topic.id,topic.name,topic.description,topic.cover,topic.user.id,topic.user.nickName,topic.createAt) from Topic topic")
    Page<TopicVo> findTopicByPage(Pageable pageable);

    @Query("SELECT new com.zsy.admin.vos.TopicVo(topic.id,topic.name,topic.description,topic.cover) from Topic topic where topic.user.id= :userId")
    List<TopicVo> getUserTopicVo(Integer userId);

    @Query("SELECT " +
            "new com.zsy.admin.vos.BlogVo(blog.id,blog.title,blog.description,blog.coverImage,blog.createAt," +
            "blog.user.id,blog.user.nickName) " +
            "from Blog blog where blog.topic.id = :tid")
    Page<BlogVo> getTopicBlogByPage(Pageable pageable, Integer tid);

    @Query("SELECT " +
            "new com.zsy.admin.vos.SimpleBlogVo(blog.id,blog.title) " +
            "from Blog blog where blog.topic.id = :tid order by blog.createAt asc")
    List<SimpleBlogVo> findAllSimpleBlog(Integer tid);
}
