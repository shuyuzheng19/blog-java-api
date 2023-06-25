package com.zsy.admin.service.db.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.entity.*;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.repository.BlogAiRepository;
import com.zsy.admin.repository.TopicRepository;
import com.zsy.admin.request.BlogRequest;
import com.zsy.admin.request.admin.FilterRequest;
import com.zsy.admin.service.search.SearchBlogService;
import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.utils.JsonUtils;
import com.zsy.admin.vos.*;
import com.zsy.admin.enums.BlogSortEnum;
import com.zsy.admin.repository.BlogRepository;
import com.zsy.admin.request.BlogPageRequest;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.service.db.BlogService;
import com.zsy.admin.vos.admin.BlogVoA;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 郑书宇
 * @create 2023/6/4 19:39
 * @desc
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogRepository blogRepository;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private BlogAiRepository blogAiRepository;

    @Resource
    private TopicRepository topicRepository;

    @Resource
    private SearchBlogService searchBlogService;


    @Override
    public PageInfo<BlogVo> paginateAndSortQueries(BlogPageRequest pageRequest) {

        Pageable pageable =getPageable(pageRequest);

        Page<BlogVo> page = null;

        if(pageRequest.getCid()>0) {
            page = blogRepository.paginateAndSortQueries(pageable, pageRequest.getCid());
        }else{
            page = blogRepository.paginateAndSortQueries(pageable);
        }

        return GlobalUtils.getPageInfo(page);
    }

    @Override
    public BlogContentVo getBlog(Integer id) throws JsonProcessingException {
        final String KEY = RedisConstants.BLOG_INFO;
        final String idStr = String.valueOf(id);
        Boolean flag = redisTemplate.opsForHash().hasKey(KEY, idStr);
        BlogContentVo blog = null;
        if(flag) {
            String json = (String) redisTemplate.opsForHash().get(KEY,idStr);
            blog = new ObjectMapper().readValue(json, BlogContentVo.class);
        }else{
            blog = blogRepository.findById(id).orElseThrow(()-> GlobalException.builder().code(ResultCode.NOT_FOUNT_CODE).message("找不到该博客").build()).toVo();
            String json = JsonUtils.objectToJson(blog);
            redisTemplate.opsForHash().put(KEY,idStr,json);
        }
        return blog;
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        Blog result = blogRepository.save(blog);
        if(result.getId()==null){
            throw GlobalException.builder().code(ResultCode.ERROR_CODE).message("博客添加失败").build();
        }
        redisTemplate.opsForSet().add(RedisConstants.RANDOM_BLOG,new SimpleBlogVo(blog.getId(),blog.getTitle()));
        searchBlogService.saveBlog(JsonUtils.objectToJson(List.of(
                new SearchBlog(blog.getId(),blog.getTitle(),blog.getDescription())
        )));
        return result;
    }

    @Override
    public Blog updateBlog(Integer blogId,BlogRequest blogRequest) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> GlobalException.builder().code(ResultCode.NOT_FOUNT_CODE).message("找不到该博客").build());
        blog.setDescription(blogRequest.getDescription());
        blog.setTitle(blogRequest.getTitle());
        blog.setCoverImage(blogRequest.getCoverImage());
        blog.setSourceUrl(blogRequest.getCoverImage());
        blog.setContent(blogRequest.getContent());
        Integer category = blogRequest.getCategory();
        if(category!=null){
            blog.setCategory(Category.of(category));
            blog.setTags(blogRequest.getTags().stream().map(tagId-> Tag.of(tagId)).collect(Collectors.toSet()));
        }
        Integer topic = blogRequest.getTopic();
        if(topic!=null){
            blog.setTopic(topicRepository.findById(topic).orElseThrow(()->GlobalException.builder().code(ResultCode.NOT_FOUNT_CODE).message("找不到专题").build()));
        }
        blog.setMarkdown(blogRequest.isMarkdown());
        blog.setUpdateAt(new Date());
        blogRepository.save(blog);
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(blog.toVo());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        redisTemplate.opsForHash().put(RedisConstants.BLOG_INFO,String.valueOf(blog.getId()),json);
        return blog;
    }

    @Override
    public int deleteBlog(Integer userId,List<Integer> ids) {
        int result = 0;
        if(userId!=null && userId==-1) {
            result = blogRepository.deleteBlogById(ids);
        }else{
            result = blogRepository.deleteBlogByUserId(userId,ids);
        }
        return result;
    }

    @Override
    public void saveEditBlog(String content) {
        Integer userId = GlobalUtils.getCurrentUserId();
        redisTemplate.opsForHash().put(RedisConstants.SAVE_EDIT_BLOG, String.valueOf(userId),content);
    }

    @Override
    public String savePreviewBlog(String content,String uuid) {
        redisTemplate.opsForValue().set(RedisConstants.SAVE_PREVIEW_BLOG+uuid,content,5, TimeUnit.MINUTES);
        return uuid;
    }

    @Override
    public String getSaveEditBlog() {
        Integer userId = GlobalUtils.getCurrentUserId();
       String content = (String) redisTemplate.opsForHash().get(RedisConstants.SAVE_EDIT_BLOG, String.valueOf(userId));
       return content;
    }

    @Override
    public String getSavePreviewBlog(String uuid) {
        String content = (String) redisTemplate.opsForValue().get(RedisConstants.SAVE_PREVIEW_BLOG + uuid);
        return content;
    }

    @Override
    public List<SimpleBlogVo> getRecommendBlog() {
        List<SimpleBlogVo> result = (List<SimpleBlogVo>) redisTemplate.opsForValue().get(RedisConstants.RECOMMEND_BLOG);
        return result;
    }

    @Override
    public List<SimpleBlogVo> getHotBlogs() {
        List<SimpleBlogVo> blogs = (List<SimpleBlogVo>) redisTemplate.opsForValue().get(RedisConstants.HOT_BLOG);
        return blogs;
    }

    @Override
    public Set<SimpleBlogVo> getRandomBlog() {
        Set<SimpleBlogVo> blogs = redisTemplate.opsForSet().distinctRandomMembers(RedisConstants.RANDOM_BLOG,Constants.RANDOM_BLOG_COUNT);
        return blogs;

    }

    @Override
    public PageInfo<ArchiveBlogVo> getBetBetweenBlog(int page,Date start, Date end) {
        Page<ArchiveBlogVo> pageResult = blogRepository.findBetweenDateBlog(
                PageRequest.of(page,Constants.ARCHIVE_BLOG_PAGE_SIZE,Sort.by(Sort.Order.desc("createAt"))).previousOrFirst(),
                start,end
        );
        return GlobalUtils.getPageInfo(pageResult);
    }

    @Override
    public PageInfo<BlogVo> getUseBlog(Integer page,Integer userId) {
        Pageable pageable = PageRequest.of(page, Constants.BLOG_PAGE_SIZE,Sort.by(Sort.Order.desc("createAt"))).previousOrFirst();
        Page<BlogVo> pageResult = blogRepository.findByUserIdBlogs(pageable, userId);
        return GlobalUtils.getPageInfo(pageResult);
    }

    @Override
    public List<SimpleBlogVo> getUserTopBlog(Integer userId) {
        List<SimpleBlogVo> blogs = blogRepository.findUserBlogTop(PageRequest.of(1,Constants.USER_TOP_BLOG_COUNT).previousOrFirst(),userId);
        return blogs;
    }

    @Override
    public BlogAi aiToDb(Integer bid,BlogAi blogAi) {
        BlogAi result = blogAiRepository.save(blogAi);
        if(result.getId()!=null){
            blogAiRepository.updateAi(result.getId(),bid);
            redisTemplate.opsForHash().delete(RedisConstants.BLOG_INFO,String.valueOf(bid));
        }
        return result;
    }

    @Override
    public PageInfo<BlogVoA> findConditionQuery(Integer userId, FilterRequest filterRequest) {
        PageInfo<BlogVoA> pageInfo = filterCondition(userId, filterRequest);
        return pageInfo;
    }

    private Pageable getPageable(BlogPageRequest blogPageRequest){
        return getPageable(blogPageRequest.getPage(),blogPageRequest.getSort());
    }

    private Pageable getPageable(int page,BlogSortEnum sortEnum){
        Sort sort  = null;
        if(sortEnum.equals(BlogSortEnum.BACK)) {
            sort=Sort.by(Sort.Order.asc(sortEnum.getField()));
        }else{
            sort=Sort.by(Sort.Order.desc(sortEnum.getField()));
        }
        Pageable pageable = PageRequest.of(page, Constants.BLOG_PAGE_SIZE,sort).previousOrFirst();
        return pageable;
    }

    public PageInfo<BlogVoA> filterCondition(Integer userId,FilterRequest filterRequest){
        Pageable pageable = getPageable(filterRequest.getPage(),filterRequest.getSort());
        Page<Blog> page = blogRepository.findAll((root,query,build)->{
            List<Predicate> predicates=new ArrayList<>();
            predicates.add(build.isNotNull(root.get("category")));
            if(userId!=null && userId>0){
                predicates.add(build.equal(root.get("user").get("id"),userId));
            }
            if(filterRequest.getCid()>0){
                predicates.add(build.equal(root.get("category").get("id"),filterRequest.getCid()));
            }
            if(!StringUtils.isEmpty(filterRequest.getKeyword())){
                String keyword = filterRequest.getKeyword();
                predicates.add(
                        build.or(
                                build.like(root.get("title"),"%"+keyword+"%"),
                                build.like(root.get("description"),"%"+keyword+"%")
                        )
                );
            }
            if(filterRequest.getRange()!=null && filterRequest.getRange().length==2){
                Long[] range = filterRequest.getRange();
                predicates.add(build.between(root.get("createAt"),new Date(range[0]),new Date(range[1])));
            }
            return build.and(predicates.toArray(new Predicate[]{}));
        },pageable);

        PageInfo<BlogVoA> pageInfo=new PageInfo<>();
        pageInfo.setPage(page.getNumber()+1);
        pageInfo.setSize(page.getSize());
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setData(page.getContent().stream().map(b->{
            BlogVoA blogVoA=new BlogVoA();
            blogVoA.setId(b.getId());
            blogVoA.setTitle(b.getTitle());
            blogVoA.setDescription(b.getDescription());
            blogVoA.setCoverImage(b.getCoverImage());
            blogVoA.setEyeCount(b.getEyeCount());
            blogVoA.setLikeCount(b.getLikeCount());
            blogVoA.setCategory(b.getCategory().toVo());
            blogVoA.setMarkdown(b.isMarkdown());
            blogVoA.setCreateAt(b.getCreateAt());
            blogVoA.setUpdateAt(b.getUpdateAt());
            return blogVoA;
        }).collect(Collectors.toList()));
        return pageInfo;
    }

}
