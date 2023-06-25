package com.zsy.admin.service.ty;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.repository.BlogRepository;
import com.zsy.admin.vos.SimpleBlogVo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/5 17:44
 * @desc
 */
@Service
public class SuperAdminServiceImpl implements SuperAdminService{

    @Resource
    private BlogRepository blogRepository;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void setRecommendBlog(List<Integer> ids) {
        int count = Constants.RECOMMEND_COUNT;
        if(ids.size()< count){
            throw GlobalException.builder().code(ResultCode.PARAMS_CODE).message("推荐博客ID不能小于"+count+"个 如果大于取前"+count+"个").build();
        }

        ids=ids.subList(0,count);

        List<SimpleBlogVo> blogs = blogRepository.findByIdIn(ids);

        redisTemplate.opsForValue().set(RedisConstants.RECOMMEND_BLOG,blogs);

    }
}
