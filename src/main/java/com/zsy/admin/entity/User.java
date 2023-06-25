package com.zsy.admin.entity;

import com.zsy.admin.constants.UserConstants;
import com.zsy.admin.enums.UserStatusEnum;
import com.zsy.admin.vos.CommentUserVo;
import com.zsy.admin.vos.SimpleUserVo;
import com.zsy.admin.vos.UserVo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;
import java.io.Serializable;
import java.util.*;

/**
 * @author 郑书宇
 * @create 2023/6/3 22:18
 * @desc
 */
@Entity
@Table(name = "users")
@Data
@Where(clause = "deleted=false")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
    用户的账号
    */
    @Column(name="username",unique = true,length = 20,nullable = false)
    private String username;

    /*
    用户的名称
    */
    @Column(name="nick_name",length = 20,nullable = false)
    private String nickName;

    /*
    用户的密码
    */
    @Column(name="password",length = 500,nullable = false)
    private String password;

    /*
    用户的头像
    */
    @Column(name="icon",length = 500,nullable = false)
    private String icon;

    /*
    用户的邮箱
    */
    @Column(name="email",unique = true,nullable = false)
    private String email;

    /*
    用户创建时间
    */
    @Column(name="create_date",nullable = false)
    private Date createAt;

    /*
    用户修改时间
    */
    @Column(name="update_date")
    private Date updateAt;

    /*
    用户状态
    */
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    /*
    该用户的角色
    */
    @OneToOne(targetEntity = Role.class,fetch = FetchType.EAGER)
    @JoinColumn(columnDefinition = "id",referencedColumnName = "id")
    private Role role;

    /*
    是否为删除状态
     */
    private boolean deleted;

    public static User of(String username, String password,String icon,String nickName,String email, Role role){
        User user =  new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setIcon(icon);
        user.setNickName(nickName);
        user.setEmail(email);
        user.setCreateAt(new Date());
        user.setUpdateAt(new Date());
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setRole(role);
        return user;
    }

    public static User of(String username, String password, String nickName,String email){
        return of(username,password,"/favicon.ico",nickName,email,UserConstants.DEFAULT_ROLE);
    }

    public static User of(Integer id){
        User user=new User();
        user.setId(id);
        return user;
    }

    public static User of(String username, String password,String icon,String nickName,String email){
        return of(username,password,icon,nickName,email,UserConstants.DEFAULT_ROLE);
    }

    public CommentUserVo toCommentUserVo(){
        CommentUserVo commentUserVo=new CommentUserVo();
        commentUserVo.setLevel(0);
        commentUserVo.setAvatar(icon);
        commentUserVo.setUsername(nickName);
        commentUserVo.setHomeLink("/user/"+id);
        return commentUserVo;
    }

    public UserVo toUserVo(){
        UserVo userVo=new UserVo();
        userVo.setId(id);
        userVo.setNickName(nickName);
        userVo.setIcon(icon);
        userVo.setStatus(status);
        userVo.setRole(role.getRoleName());
        return userVo;
    }

    public SimpleUserVo toSimpleUserVo(){
        SimpleUserVo userVo=new SimpleUserVo();
        userVo.setId(id);
        userVo.setNickName(nickName);
        return userVo;
    }
}
