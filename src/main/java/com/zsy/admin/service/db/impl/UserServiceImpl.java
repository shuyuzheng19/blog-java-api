package com.zsy.admin.service.db.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.entity.User;
import com.zsy.admin.enums.RoleEnum;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.repository.UserRepository;
import com.zsy.admin.request.admin.LoginRequest;
import com.zsy.admin.response.Result;
import com.zsy.admin.response.admin.LoginResponse;
import com.zsy.admin.service.db.UserService;
import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.utils.JsonUtils;
import com.zsy.admin.utils.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author 郑书宇
 * @create 2023/6/4 0:29
 * @desc
 */

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Optional<User> findByUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUserByUsername(String username) {
        String key = RedisConstants.USER_INFO + username;
        Boolean exists = redisTemplate.hasKey(key);
        if(exists) {
            User user = null;
            try {
                user = new ObjectMapper().readValue(redisTemplate.opsForValue().get(key).toString(), User.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Optional.of(user);
        }else{
            User user = userRepository.findByUsername(username).orElse(null);
            try {
                redisTemplate.opsForValue().set(key,new ObjectMapper().writeValueAsString(user),RedisConstants.USER_INFO_EXPIRE, TimeUnit.MINUTES);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Optional.of(user);
        }
    }

    @Override
    public void registerUser(User user) {
        boolean existsUsername = userRepository.existsByUsername(user.getUsername());
        if(existsUsername) throw GlobalException.builder().code(ResultCode.NOT_FOUNT_CODE).message("该用户已存在,换个账号吧~").build();
        boolean existsEmail = userRepository.existsByEmail(user.getEmail());
        if(existsEmail) throw GlobalException.builder().code(ResultCode.NOT_FOUNT_CODE).message("该邮箱已存在,可以通过邮箱修改密码哦~").build();
        User result = userRepository.save(user);
        if(result.getId()==null){
            throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("注册失败").build();
        }
    }

    @Override
    public boolean verifyToken(String username, String sourceToken) {
        Object targetToken = redisTemplate.opsForValue().get(RedisConstants.USER_TOKEN + username);
        if(Objects.isNull(targetToken)) return false;
        return sourceToken.equals(targetToken.toString());
    }

    @Override
    public void logout() {
        String username = GlobalUtils.getCurrentUser().getUsername();
        redisTemplate.delete(RedisConstants.USER_TOKEN+username);
    }
//
//    @Override
//    public LoginResponse loginAdmin(LoginRequest request) {
//
//        User user = findByUserByUsername(request.getAccount()).orElseThrow(()->GlobalException.builder().code(ResultCode.ERROR_CODE).message("账号不存在").build());
//
//        String password = request.getPassword();
//
//        if(passwordEncoder.matches(password,user.getPassword())){
//            throw GlobalException.builder().code(ResultCode.ERROR_CODE).message("错误的密码").build();
//        }
//
//        if(user.getRole().equals(RoleEnum.USER)) throw GlobalException.builder().code(ResultCode.ERROR_CODE).message("普通用户,禁止登录").build();
//
//        String username = user.getUsername();
//
//        Object token = redisTemplate.opsForValue().get(RedisConstants.USER_TOKEN + username);
//
//        String accessToken= JwtUtils.generateJwt(user.getUsername(), Map.of("roles",user.getRole().toString()));
//
//        if(Objects.isNull(token)){
//            redisTemplate.opsForValue().set(RedisConstants.USER_TOKEN+username,accessToken,Constants.TOKEN_EXPIRE, TimeUnit.DAYS);
//        }
//
//        LoginResponse response = LoginResponse.builder().lastLoginTime(new Date()).account(user.getNickName()).avatar(user.getIcon()).uid(user.getId()).build();
//
//        return response;
//    }

    @Override
    public Optional<User> findByUserByUsernameAndPassword(String username,String password) {
        return userRepository.findUserByUsernameAndPassword(username,password);
    }
}
