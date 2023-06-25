package com.zsy.admin.entity;

import com.zsy.admin.vos.CategoryVo;
import com.zsy.admin.vos.TagVo;
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
@Table(name="categories")
@Where(clause = "deleted=false")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
    分类名子
     */
    @Column(name="name",length = 20,nullable = false,unique = true)
    private String name;

    /*
    分类创建时间
    */
    private Date createAt;

    /*
    是否为删除状态
     */
    private boolean deleted;

    public static Category of(String name){
        Category category=new Category();
        category.setName(name);
        category.setCreateAt(new Date());
        return category;
    }

    public static Category of(Integer id){
        Category category=new Category();
        category.setId(id);
        return category;
    }

    public CategoryVo toVo(){
        CategoryVo category=new CategoryVo();
        category.setId(id);
        category.setName(name);
        return category;
    }
}
