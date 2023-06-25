package com.zsy.admin.service;

import com.zsy.admin.entity.User;
import com.zsy.admin.service.db.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author 郑书宇
 * @create 2023/6/4 0:25
 * @desc
 */

@Service("myUserDetailService")
public class MyUserDetailService implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("该用户不存在"));
        return new MyUserDetails(user);
    }

    public boolean verifyToken(String username,String sourceToken){
        return userService.verifyToken(username,sourceToken);
    }


}
