package com.zsy.admin.service;

import com.zsy.admin.entity.User;
import com.zsy.admin.enums.UserStatusEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 郑书宇
 * @create 2023/6/4 16:40
 * @desc
 */
public class MyUserDetails implements UserDetails {

    private User user;

    public MyUserDetails(User user){
        this.user=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list= new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_"+user.getRole().getRoleName()));
        return list;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus().equals(UserStatusEnum.ACTIVE);
    }

    public User getUser() {
        return user;
    }
}
