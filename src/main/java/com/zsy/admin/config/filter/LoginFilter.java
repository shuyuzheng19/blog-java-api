package com.zsy.admin.config.filter;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.enums.RoleEnum;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.response.Result;
import com.zsy.admin.utils.IpUtils;
import com.zsy.admin.utils.JsonUtils;
import com.zsy.admin.utils.JwtUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 郑书宇
 * @create 2023/6/4 1:31
 * @desc
 */
@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String method = request.getMethod();

        if (method.equals("OPTIONS") || !request.getMethod().equals("POST")) {
            return null;
        }

        String username = request.getParameter("username");

        String password = request.getParameter("password");

        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
            JsonUtils.writeJsonToOutputStream(Result.fail(ResultCode.PARAMS_CODE.getCode(), "账号或密码不能为空!"),response );

        String code = request.getParameter("code");

        String ip = IpUtils.getIpAddress(request);

        String key = RedisConstants.CAPTCHA_CODE + ip;

        String resultCode =stringRedisTemplate.opsForValue().get(key);

        if(StringUtils.isEmpty(resultCode) || !resultCode.equals(code)) {
            JsonUtils.writeJsonToOutputStream(Result.fail("验证码错误"), response);
            return null;
        }

        var usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(username,password);

        stringRedisTemplate.delete(key);

        try {
            return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
        }catch (Exception e){
            JsonUtils.writeJsonToOutputStream(Result.fail(ResultCode.ERROR_CODE.getCode(), "账号或密码错误"),response);
            return null;
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String ip = IpUtils.getIpAddress(request);
        resetFailureCount(ip,response);
        UserDetails principal = (UserDetails) authResult.getPrincipal();
        boolean admin = Boolean.valueOf(request.getParameter("admin"));
        if(admin) {
            boolean isUser = principal.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals(RoleEnum.USER.name()));
            if(isUser){
                JsonUtils.writeJsonToOutputStream(Result.fail("普通用户,不允许登录后台管理"),response);
                return;
            }
        }
        log.info("用户登录成功=>用户名:{} 所有权限:{} 用户登录地址:{}",principal.getUsername(),principal.getAuthorities(),IpUtils.getIpCity(ip));
        String accessToken= JwtUtils.generateJwt(principal.getUsername(), Map.of("roles",principal.getAuthorities().toString()));
        JsonUtils.writeJsonToOutputStream(Result.success(accessToken),response);
        redisTemplate.opsForValue().set(RedisConstants.USER_TOKEN+principal.getUsername(),accessToken,Constants.TOKEN_EXPIRE, TimeUnit.DAYS);
    }

    private void incrementFailureCount(String ip) {
        String key = RedisConstants.LOGIN_FAIL_COUNT + ip;
        redisTemplate.opsForValue().increment(key, 1);
        redisTemplate.expire(key, RedisConstants.DEFAULT_LOCK_IP_EXPIRE, TimeUnit.MINUTES);
    }

    private int getFailureCount(String ip) {
        String key = RedisConstants.LOGIN_FAIL_COUNT  + ip;
        Integer count = (Integer) redisTemplate.opsForValue().get(key);
        return count != null ? count : 0;
    }

    private void resetFailureCount(String ip,HttpServletResponse response) {
        String key  =  RedisConstants.LOGIN_FAIL_COUNT+ip;
        if(getFailureCount(ip)>=10) {
            Long expire = redisTemplate.getExpire(key,TimeUnit.MINUTES);
            log.error("账号密码正确 但是IP已被封禁 IP:{}",ip);
            JsonUtils.writeJsonToOutputStream(Result.fail(ResultCode.ERROR_CODE.getCode(), "你的IP已被锁定 "+expire +" 分钟后解除锁定"),response);
        }else{
            redisTemplate.delete(key);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        if(failed instanceof BadCredentialsException) {
            String ip =  IpUtils.getIpAddress(request);
            int failureCount = getFailureCount(ip);
            if (failureCount >= 10) {
                Long expire = redisTemplate.getExpire(RedisConstants.LOGIN_FAIL_COUNT + ip, TimeUnit.MINUTES);
                JsonUtils.writeJsonToOutputStream(Result.fail(ResultCode.ERROR_CODE.getCode(), "你的IP已被锁定 " + expire + " 分钟后解除锁定"), response);
                log.error("因频繁输错账号密码 IP已被封禁 IP:{}", ip);
                return;
            }else{
                incrementFailureCount(ip);
            }
            JsonUtils.writeJsonToOutputStream(Result.fail(ResultCode.PARAMS_CODE.getCode(), "账号或密码错误"),response);
        }
    }
}
