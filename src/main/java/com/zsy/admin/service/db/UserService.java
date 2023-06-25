package com.zsy.admin.service.db;

import com.zsy.admin.entity.User;
import com.zsy.admin.request.admin.LoginRequest;
import com.zsy.admin.response.admin.LoginResponse;

import java.util.Optional;

/**
 * @author 郑书宇
 * @create 2023/6/4 0:26
 * @desc
 */
public interface UserService {
    Optional<User> findByUserById(Integer id);
    Optional<User> findByUserByUsernameAndPassword(String username,String password);
    //通过用户名获取用户
    Optional<User> findByUserByUsername(String username);
    //注册账号
    void registerUser(User user);
    //验证TOKEN
    boolean verifyToken(String username,String sourceToken);
    //退出当前用户登录
    void logout();
}
