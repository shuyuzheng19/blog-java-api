package com.zsy.admin.request;

import com.zsy.admin.entity.Topic;
import com.zsy.admin.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/5 12:48
 * @desc
 */
@Data
public class TopicRequest {

    @NotEmpty(message = "专题名不能为空")
    @Length(max=15,message = "专题名称不能大于15个字符")
    private String name;

    @Pattern(regexp = "^https?://.*\\.(png|jpe?g|gif)$",message = "这不是一个正确图片链接")
    private String cover;

    @NotEmpty(message = "专题简介不能为空")
    @Length(max=150,message = "专题简介不能大于150个字符")
    private String desc;

    public Topic toTopic(Integer userId){
        Topic topic=new Topic();
        topic.setName(name);
        topic.setCover(cover);
        topic.setUser(User.of(userId));
        topic.setDescription(desc);
        topic.setCreateAt(new Date());
        return topic;
    }
}
