package com.zsy.admin.service;

import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.response.BlogInfo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author 郑书宇
 * @create 2023/6/5 20:57
 * @desc
 */
@Service
public class ConfigService {

    @Resource
    private RedisTemplate redisTemplate;

    public BlogInfo getConfig(){
        BlogInfo blogInfo = (BlogInfo) redisTemplate.opsForValue().get(RedisConstants.BLOG_CONFIG);
        if(blogInfo==null){
            return BlogInfo.defaultInfo();
        }
        return blogInfo;
    }

}
