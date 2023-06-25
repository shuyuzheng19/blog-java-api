package com.zsy.admin.service.ty;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.entity.Like;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.repository.LikeRepository;
import io.lettuce.core.ScriptOutputType;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * @author 郑书宇
 * @create 2023/6/7 14:09
 * @desc
 */
@Service
public class LikeServiceImpl implements LikeService{

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private LikeRepository likeRepository;

    @Override
    public void like(Integer blogId, String ip) {
        boolean like = isLike(blogId, ip);
        if(like) {
            throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("你已经点过赞了!").build();
        }else{
            Like l=new Like();
            l.setLikeDate(new Date());
            l.setBlogId(blogId);
            l.setIp(ip);
            Like result = likeRepository.save(l);
            redisTemplate.opsForSet().add(RedisConstants.IP_LIKE + ip, blogId);
            if(result.getId()==null) {
                throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("你已经点过赞了").build();
            }
        }
    }

    @Override
    public boolean isLike(Integer blogId, String ip) {
        final String KEY=RedisConstants.IP_LIKE+ip;
        boolean like = redisTemplate.opsForSet().isMember(KEY,blogId);
        return like;
    }
}
