package com.zsy.admin.config;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.entity.Like;
import com.zsy.admin.enums.UploadTypeEnum;
import com.zsy.admin.repository.BlogRepository;
import com.zsy.admin.repository.CategoryRepository;
import com.zsy.admin.repository.LikeRepository;
import com.zsy.admin.repository.TagRepository;
import com.zsy.admin.service.search.SearchBlogService;
import com.zsy.admin.utils.JsonUtils;
import com.zsy.admin.vos.CategoryVo;
import com.zsy.admin.vos.SearchBlog;
import com.zsy.admin.vos.SimpleBlogVo;
import com.zsy.admin.vos.TagVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 郑书宇
 * @create 2023/6/5 18:49
 * @desc
 */
@Component
@Slf4j
public class GlobalTask implements CommandLineRunner {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private BlogRepository blogRepository;

    @Resource
    private TagRepository tagRepository;

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private LikeRepository likeRepository;

    @Resource
    private SearchBlogService searchBlogService;

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${first}")
    private boolean first;

    @Value("${initSearch}")
    private boolean initSearch;

    @Scheduled(fixedRate = 60*60*5*1000)
    public void updateEyeCount() {
        log.info("开始更新浏览量.........");
        Map<String,Integer> map = redisTemplate.opsForHash().entries(RedisConstants.BLOG_EYE_COUNT_MAP);
        if(map!=null && map.size()>0){
            for (String id : map.keySet()) {
                blogRepository.updateEyeCount(Integer.valueOf(id),map.get(id));
            }
        }
        List<SimpleBlogVo> blogs = blogRepository.selectOrderByEyeCount(PageRequest.of(1, Constants.HOT_BLOG_COUNT).previousOrFirst());
        redisTemplate.opsForValue().set(RedisConstants.HOT_BLOG,blogs);
        log.info("浏览量更新完毕 博客排行榜已重置");
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void updateTagAndCategoryAndBlog() {
        init();
        log.info("同步用户点赞");
        Set<String> ips = likeRepository.findAllIp();
        redisTemplate.delete(redisTemplate.keys(RedisConstants.IP_LIKE+"*"));
        for (String ip : ips) {
            String[] blogIds = likeRepository.findLike(ip);
            redisTemplate.opsForSet().add(RedisConstants.IP_LIKE+ip,blogIds);
        }
    }

    private void init(){
        log.info("开始初始化,随机标签,随机博客,分类列表");
        final String RANDOM_TAG_KEY = RedisConstants.RANDOM_TAG;
        final String RANDOM_BLOG_KEY = RedisConstants.RANDOM_BLOG;
        final String LIST_CATEGORY_KEY = RedisConstants.CATEGORY_LIST;
        redisTemplate.delete(List.of(RANDOM_TAG_KEY,RANDOM_BLOG_KEY,LIST_CATEGORY_KEY,RedisConstants.TOPIC_MAP));
        List<TagVo> tags = tagRepository.findAllTag();
        redisTemplate.opsForSet().add(RANDOM_TAG_KEY,tags.toArray(new TagVo[]{}));
        log.info("随机标签初始化完成");
        List<CategoryVo> categoryList = categoryRepository.findAllCategory();
        redisTemplate.opsForList().leftPushAll(LIST_CATEGORY_KEY,categoryList);
        log.info("分类列表初始化完成");
        List<SimpleBlogVo> blogs = blogRepository.findAllSimpleBlog();
        redisTemplate.opsForSet().add(RANDOM_BLOG_KEY,blogs.toArray(new SimpleBlogVo[]{}));
        log.info("随机博客初始化完成");
    }

    @Scheduled(fixedRate = 60*60*15*1000)
    public void updateLikeCount() {
        log.info("开始更新点赞量.........");
        Map<String,Integer> map = redisTemplate.opsForHash().entries(RedisConstants.BLOG_LIKE_COUNT_MAP);
        if(map!=null && map.size()>0){
            for (String id : map.keySet()) {
                blogRepository.updateLikeCount(Integer.valueOf(id),map.get(id));
            }
        }
        log.info("点赞量更新完毕");
    }

    private void initSearch(){
        List<SearchBlog> allSearchBlog = blogRepository.findAllSearchBlog();
        String json = JsonUtils.objectToJson(allSearchBlog);
        searchBlogService.saveBlog(json);
    }

    @Override
    public void run(String... args) throws Exception {
        File file=new File(uploadPath+"/"+ UploadTypeEnum.AVATAR.getType());
        if(!file.exists()) file.mkdirs();
        File file2=new File(uploadPath+"/"+ UploadTypeEnum.IMAGES.getType());
        if(!file2.exists()) file2.mkdirs();
        File file3=new File(uploadPath+"/"+ UploadTypeEnum.FILES.getType());
        if(!file3.exists()) file3.mkdirs();
        if(first) {
            init();
        }
        if(initSearch){
            log.info("初始化搜索引擎");
            searchBlogService.deleteAllDocument();
            initSearch();
            log.info("初始化搜索引擎完毕");
        }
    }
}
