package com.zsy.admin.entity;

import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.vos.SimpleTopicVo;
import com.zsy.admin.vos.TopicVo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/4 17:28
 * @desc
 */
@Data
@Entity
@Table(name="topics")
@Where(clause = "deleted=false")
public class Topic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
    专题名子
     */
    @Column(name="name",length = 20,nullable = false,unique = true)
    private String name;

    /*
    专题介绍
     */
    @Column(name="description",nullable = false)
    private String description;

    /*
    专题封面
     */
    @Column(name="cover_image",nullable = false)
    private String cover;


    @OneToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    private User user;

    /*
    专题创建时间
    */
    private Date createAt;

    /*
    是否为删除状态
     */
    private boolean deleted;

    public static Topic of(Integer id){
        Topic topic=new Topic();
        topic.setId(id);
        return topic;
    }

    public SimpleTopicVo toSimpleTopicVo(){
        SimpleTopicVo topicVo=new SimpleTopicVo();
        topicVo.setId(id);
        topicVo.setName(name);
        return topicVo;
    }

    public TopicVo toVo(){
        TopicVo topicVo=new TopicVo();
        topicVo.setId(id);
        topicVo.setName(name);
        topicVo.setDescription(description);
        topicVo.setCover(cover);
        topicVo.setUser(user.toSimpleUserVo());
        topicVo.setDateStr(GlobalUtils.dateToStr(user.getCreateAt()));
        return topicVo;
    }
}
