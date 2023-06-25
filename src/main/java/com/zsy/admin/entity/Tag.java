package com.zsy.admin.entity;

import com.zsy.admin.vos.TagVo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/4 17:28
 * @desc
 */
@Data
@Entity
@Table(name="tags")
@Where(clause = "deleted=false")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
    标签名子
     */
    @Column(name="name",length = 20,nullable = false,unique = true)
    private String name;

    /*
    标签创建时间
    */
    private Date createAt;

    /*
    是否为删除状态
     */
    private boolean deleted;


    public static Tag of(String name){
        Tag tag=new Tag();
        tag.setName(name);
        tag.setCreateAt(new Date());
        return tag;
    }

    public static Tag of(Integer id){
        Tag tag=new Tag();
        tag.setId(id);
        return tag;
    }

    public TagVo toVo(){
        TagVo tagVo=new TagVo();
        tagVo.setId(id);
        tagVo.setName(name);
        return tagVo;
    }
}
