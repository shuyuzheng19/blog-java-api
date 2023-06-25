package com.zsy.admin.service.db;

import com.zsy.admin.entity.Topic;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.vos.BlogVo;
import com.zsy.admin.vos.SimpleBlogVo;
import com.zsy.admin.vos.SimpleTopicVo;
import com.zsy.admin.vos.TopicVo;

import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/5 9:39
 * @desc
 */
public interface TopicService {
    //获取用户专题列表
    List<SimpleTopicVo> getSimpleTopicVos(Integer userId);
    //查询所有专题并且分页
    PageInfo<TopicVo> getByPageTopic(int page);
    //添加一个专题
    Topic saveTopic(Topic topic);
    //获取用户专题列表
    List<TopicVo> getUserTopics(Integer userId);
    //获取当前用户专题列表
    List<TopicVo> getCurrentTopics();
    //获取专题下的博客
    PageInfo<BlogVo> getTopicBlog(int page, Integer topicId);
    //获取专题信息
    TopicVo getTopicById(Integer id);
    //获取专题列表
    List<SimpleBlogVo> findAllSimpleBlog(Integer tid);
}
