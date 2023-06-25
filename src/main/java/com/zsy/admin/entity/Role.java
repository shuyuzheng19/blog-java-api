package com.zsy.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/3 22:34
 * @desc
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=false")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
    角色名字
     */
    @Column(name="role_name",nullable = false,unique = true,length = 20)
    private String roleName;

    /*
    该角色的描述信息
     */
    @Column(name="description",nullable = false)
    private String description;

    /*
    该角色的创建时间
     */
    @Column(name="create_at")
    private Date createAt;

    /*
    是否为删除状态
     */
    private boolean deleted;

    public static Role of(Integer id,String roleName,String description){
        return new Role(id,roleName,description,new Date(),false);
    }

    public static Role of(Integer id){
        return new Role(id,null,null,null,false);
    }
}
