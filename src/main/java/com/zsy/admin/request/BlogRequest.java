package com.zsy.admin.request;

import com.zsy.admin.entity.*;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 郑书宇
 * @create 2023/6/5 12:01
 * @desc
 */
@Data
public class BlogRequest {

    @NotEmpty(message = "博客标题不能为空")
    @Length(max = 50,message = "博客标题最大不能大于50个字符")
    private String title;

    @NotEmpty(message = "博客描述不能为空")
    @Length(max = 200,message = "博客描述最大不能大于200个字符")
    private String description;

    @NotEmpty(message = "博客内容不能为空")
    private String content;

    private boolean markdown=true;

    private String sourceUrl;

    @Pattern(regexp = "^https?://.*\\.(png|jpe?g|gif)$",message = "这不是一个正确图片链接")
    private String coverImage;

    private Set<Integer> tags=new HashSet<>();

    private Integer topic=null;

    private Integer category;

    public Blog toBlogDo(Integer userId){
        Blog blog=new Blog();
        blog.setDescription(description);
        blog.setTitle(title);
        blog.setCoverImage(coverImage);
        blog.setSourceUrl(sourceUrl);
        blog.setContent(content);
        blog.setEyeCount(0L);
        blog.setLikeCount(0L);
        blog.setStarCount(0L);
        if(category!=null&&category>0) {
            blog.setCategory(Category.of(category));
            if (tags.size() > 0) {
                blog.setTags(tags.stream().map(tagId -> Tag.of(tagId)).collect(Collectors.toSet()));
            } else {
                throw GlobalException.builder().code(ResultCode.PARAMS_CODE).message("标签不能为空").build();
            }
        }else{
            if(topic!=null && topic>0) {
                blog.setTopic(Topic.of(topic));
            }else{
                throw GlobalException.builder().code(ResultCode.PARAMS_CODE).message("专题不能为空或者分类不能为空").build();
            }
        }
        blog.setUser(User.of(userId));
        blog.setMarkdown(markdown);
        blog.setCreateAt(new Date());
        blog.setUpdateAt(new Date());
        blog.setDeleted(false);
        return blog;
    }
}
