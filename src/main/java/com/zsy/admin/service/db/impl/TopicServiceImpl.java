package com.zsy.admin.service.db.impl;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.entity.Topic;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.repository.TopicRepository;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.service.db.TopicService;
import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.utils.JsonUtils;
import com.zsy.admin.vos.BlogVo;
import com.zsy.admin.vos.SimpleBlogVo;
import com.zsy.admin.vos.SimpleTopicVo;
import com.zsy.admin.vos.TopicVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

/**
 * @author 郑书宇
 * @create 2023/6/5 9:39
 * @desc
 */
@Service
@Slf4j
public class TopicServiceImpl implements TopicService {

    @Resource
    private TopicRepository topicRepository;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public List<SimpleTopicVo> getSimpleTopicVos(Integer userId) {
        return topicRepository.findSimpleTopicVoList(userId);
    }

    @Override
    public PageInfo<TopicVo> getByPageTopic(int page) {
        Page<TopicVo> pageResult = topicRepository.findTopicByPage(PageRequest.of(page, Constants.TOPIC_PAGE_SIZE, Sort.by(Sort.Order.desc("createAt"))).previousOrFirst());
        PageInfo<TopicVo> pageInfo=new PageInfo<>();
        pageInfo.setData(pageResult.getContent());
        pageInfo.setPage(pageResult.getNumber()+1);
        pageInfo.setSize(pageResult.getSize());
        pageInfo.setTotal(pageResult.getTotalElements());
        return pageInfo;
    }

    @Transactional
    @Override
    public Topic saveTopic(Topic topic) {
        try{
            Topic result = topicRepository.save(topic);
            log.info("添加专题成功 专题:{}", result);
            return result;
        }catch (Exception e){
            log.error("添加专题失败:{}",topic,e);
            throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("专题添加失败").build();
        }
    }

    @Override
    public List<TopicVo> getUserTopics(Integer userId) {
        List<TopicVo> topics = topicRepository.getUserTopicVo(userId);
        return topics;
    }

    @Override
    public List<TopicVo> getCurrentTopics() {
        List<TopicVo> topics = getUserTopics(GlobalUtils.getCurrentUserId());
        return topics;
    }

    @Override
    public PageInfo<BlogVo> getTopicBlog(int page, Integer topicId) {
        Page<BlogVo> pageResult = topicRepository.getTopicBlogByPage(PageRequest.of(page, Constants.BLOG_PAGE_SIZE, Sort.by(Sort.Order.asc("createAt"))).previousOrFirst(),topicId);
        PageInfo<BlogVo> pageInfo=new PageInfo<>();
        pageInfo.setPage(pageResult.getNumber()+1);
        pageInfo.setData(pageResult.getContent());
        pageInfo.setTotal(pageResult.getTotalElements());
        pageInfo.setSize(pageResult.getSize());
        return pageInfo;
    }

    @Override
    public TopicVo getTopicById(Integer id) {
        final String KEY= RedisConstants.TOPIC_MAP;
        final String ID_STR= String.valueOf(id);
        Boolean flag = redisTemplate.opsForHash().hasKey(KEY,ID_STR);
        if(flag){
            Object object = redisTemplate.opsForHash().get(KEY, ID_STR);
            if(Objects.isNull(object)) return null;
            TopicVo topic = JsonUtils.jsonToObject(object.toString(),TopicVo.class);
            return topic;
        }
        TopicVo topic = topicRepository.findById(id).orElseThrow(() ->
                GlobalException.builder().code(ResultCode.NOT_FOUNT_CODE).message("该专题不存在").build()).toVo();
        redisTemplate.opsForHash().put(KEY,ID_STR, JsonUtils.objectToJson(topic));
        return topic;
    }

    @Override
    public List<SimpleBlogVo> findAllSimpleBlog(Integer tid) {
        return topicRepository.findAllSimpleBlog(tid);
    }
}
