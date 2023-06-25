package com.zsy.admin.service.db.impl;

import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.entity.Category;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.repository.CategoryRepository;
import com.zsy.admin.service.db.CategoryService;
import com.zsy.admin.vos.CategoryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/5 9:38
 * @desc
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category createCategory(String tagName) {
        Category category = Category.of(tagName);
        try{
            Category result = categoryRepository.save(category);
            redisTemplate.opsForList().leftPush(RedisConstants.CATEGORY_LIST,result);
            log.info("添加分类成功 分类:{}", result);
            return result;
        }catch (Exception e){
            log.error("添加分类失败 分类名称:{}",tagName,e);
            throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("标签添加失败").build();
        }
    }

    @Override
    public List<CategoryVo> getAllCategory() {
        return categoryRepository.findAllCategory();
    }

    @Override
    public List<CategoryVo> getCategoryList() {
        List<CategoryVo> list= redisTemplate.opsForList().range(RedisConstants.CATEGORY_LIST,0,-1);
        return list;
    }
}
