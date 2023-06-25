package com.zsy.admin.entity;

import com.zsy.admin.vos.BlogContentVo;
import com.zsy.admin.vos.TagVo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 郑书宇
 * @create 2023/6/4 18:14
 * @desc
 */
@Data
@Entity
@Table(name="blogs")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Blog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="description",nullable = false)
    private String description;

    @Column(name="title",nullable = false,length = 80)
    private String title;

    @Column(name="cover_image",nullable = false)
    private String coverImage;

    @Column(name="source_url")
    private String sourceUrl;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    private Long eyeCount=0L;

    private Long likeCount=0L;

    private Long starCount=0L;

    @OneToOne(targetEntity = Category.class,fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id",columnDefinition = "id")
    private Category category;

    @ManyToMany(targetEntity = Tag.class,fetch = FetchType.LAZY)
    private Set<Tag> tags=new HashSet<>();

    @OneToOne(targetEntity = Topic.class,fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id",columnDefinition = "id")
    private Topic topic;

    @OneToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id",columnDefinition = "id")
    private User user;

    @OneToOne(targetEntity = BlogAi.class,fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id",columnDefinition = "id")
    private BlogAi blogAi;

    private boolean markdown;

    private Date createAt;

    private Date updateAt;

    private boolean deleted;

    public BlogContentVo toVo(){
        BlogContentVo blogContentVo=new BlogContentVo();
        blogContentVo.setId(id);
        blogContentVo.setDescription(description);
        blogContentVo.setTitle(title);
        blogContentVo.setCoverImage(coverImage);
        blogContentVo.setSourceUrl(sourceUrl);
        blogContentVo.setContent(content);
        blogContentVo.setEyeCount(eyeCount);
        blogContentVo.setLikeCount(likeCount);
        blogContentVo.setStarCount(starCount);
        if(category!=null){
            blogContentVo.setCategory(category.toVo());
        }
        if(blogAi!=null){
            blogContentVo.setAiMessage(blogAi.getContent());
        }
        if(tags.size()>0){
            blogContentVo.setTags(
                    tags.stream().map(tag->new TagVo(tag.getId(),tag.getName())).collect(Collectors.toSet())
            );
        }
        if(topic!=null){
            blogContentVo.setTopic(topic.toVo());
        }
        blogContentVo.setUser(user.toSimpleUserVo());
        blogContentVo.setMarkdown(markdown);
        blogContentVo.setCreateTime(createAt);
        blogContentVo.setUpdateTime(updateAt);
        return blogContentVo;
    }
}
