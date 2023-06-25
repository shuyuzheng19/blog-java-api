package com.zsy.admin.service.ty;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author 郑书宇
 * @create 2023/6/5 19:23
 * @desc
 */
@Service
public class CountServiceImpl implements CountService{

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Long increaseInView(Long defaultCount,Integer blogId) {
        if(redisTemplate.opsForHash().hasKey(RedisConstants.BLOG_EYE_COUNT_MAP,String.valueOf(blogId))) {
            Long increment = redisTemplate.opsForHash().increment(RedisConstants.BLOG_EYE_COUNT_MAP, String.valueOf(blogId), 1);
            return increment;
        }else{
            defaultCount+=1;
            redisTemplate.opsForHash().put(RedisConstants.BLOG_EYE_COUNT_MAP, String.valueOf(blogId), defaultCount);
            return defaultCount;
        }
    }

    @Override
    public Long increaseLikeView(Integer blogId) {
        Long increment = redisTemplate.opsForHash().increment(RedisConstants.BLOG_LIKE_COUNT_MAP, String.valueOf(blogId), 1);
        return increment;
    }

    @Override
    public Integer getLikeCount(Integer blogId) {
        Integer count = (Integer) redisTemplate.opsForHash().get(RedisConstants.BLOG_LIKE_COUNT_MAP, String.valueOf(blogId));
        return count;
    }
}
