package com.zsy.admin.service.db.impl;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.entity.Tag;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.repository.TagRepository;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.service.db.TagService;
import com.zsy.admin.utils.JsonUtils;
import com.zsy.admin.vos.BlogVo;
import com.zsy.admin.vos.TagVo;
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
import java.util.Set;

/**
 * @author 郑书宇
 * @create 2023/6/5 9:38
 * @desc
 */
@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Resource
    private TagRepository tagRepository;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tag createTag(String tagName) {
        Tag tag = Tag.of(tagName);
        try{
            Tag result = tagRepository.save(tag);
            log.info("添加标签成功 标签:{}", tag);
            redisTemplate.opsForSet().add(RedisConstants.RANDOM_TAG,tag.toVo());
            return result;
        }catch (Exception e){
            log.error("添加标签失败标签名称:{}", tagName,e);
            throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("标签添加失败").build();
        }
    }

    @Override
    public List<TagVo> getAllTag() {
        return tagRepository.findAllTag();
    }

    @Override
    public Set<TagVo> getRandomTag() {
        Set<TagVo> tags= redisTemplate.opsForSet().distinctRandomMembers(RedisConstants.RANDOM_TAG, Constants.RANDOM_TAG_COUNT);
        return tags;
    }

    @Override
    public PageInfo<BlogVo> getTagBlogs(int page, Integer tagId) {
        Page<BlogVo> pageResult = tagRepository.getTagBlogById(PageRequest.of(page, Constants.BLOG_PAGE_SIZE, Sort.by(Sort.Order.desc("createAt"))).previousOrFirst(), tagId);
        PageInfo<BlogVo> pageInfo=new PageInfo<>();
        pageInfo.setPage(pageResult.getNumber()+1);
        pageInfo.setData(pageResult.getContent());
        pageInfo.setTotal(pageResult.getTotalElements());
        pageInfo.setSize(pageResult.getSize());
        return pageInfo;
    }

    @Override
    public Tag getTagInfo(Integer tagId) {
        final String KEY= RedisConstants.TAG_MAP;
        final String ID_STR= String.valueOf(tagId);
        Boolean flag = redisTemplate.opsForHash().hasKey(KEY,ID_STR);
        if(flag){
            Object object = redisTemplate.opsForHash().get(KEY, ID_STR);
            if(Objects.isNull(object)) return null;
            Tag tag = JsonUtils.jsonToObject(object.toString(),Tag.class);
            return tag;
        }
        Tag tag = tagRepository.findById(tagId).orElseThrow(() ->
                GlobalException.builder().code(ResultCode.NOT_FOUNT_CODE).message("该标签不存在").build());
        redisTemplate.opsForHash().put(KEY,ID_STR, JsonUtils.objectToJson(tag));
        return tag;
    }
}
