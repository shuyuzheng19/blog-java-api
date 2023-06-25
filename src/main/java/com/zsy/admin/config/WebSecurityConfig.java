package com.zsy.admin.config;

import com.zsy.admin.config.filter.JwtFilter;
import com.zsy.admin.config.filter.LoginFilter;
import com.zsy.admin.constants.Constants;
import com.zsy.admin.enums.RoleEnum;
import com.zsy.admin.service.MyUserDetailService;
import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.constants.SecurityConstants;
import com.zsy.admin.utils.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 郑书宇
 * @create 2023/6/3 23:58
 * @desc
 */
@Configuration
public class WebSecurityConfig implements CorsConfigurationSource {

    @Resource
    private AuthenticationConfiguration authenticationConfiguration;

    @Resource(name="myUserDetailService")
    private MyUserDetailService userDetailsService;

    @Bean
    public JwtFilter jwtFilter() throws Exception {
        JwtFilter jwtFilter = new JwtFilter(authenticationConfiguration.getAuthenticationManager(),userDetailsService);
        return jwtFilter;
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter=new LoginFilter();
        loginFilter.setPostOnly(true);
        loginFilter.setFilterProcessesUrl(GlobalUtils.ofApiUrl("user","/login"));
        loginFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        return loginFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .authenticationManager(authenticationConfiguration.getAuthenticationManager())
                .authorizeHttpRequests()
                .requestMatchers(Constants.API_PREFIX+"/*/auth/**").authenticated()
                .requestMatchers(Constants.API_PREFIX+"/*/admin/**",Constants.API_PREFIX+"/admin/**")
                .hasAnyRole(RoleEnum.ADMIN.name(),RoleEnum.SUPER_ADMIN.name())
                .requestMatchers(Constants.API_PREFIX+"/super/**").hasRole(RoleEnum.SUPER_ADMIN.name())
                .anyRequest().permitAll().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint((request,response,exception)->{
                    response.setStatus(200);
                    Map<String,Object> map=new HashMap<>();
                    map.put("code",403);
                    map.put("error_url",request.getRequestURL());
                    map.put("message","该操作需要登录,请先登录");
                    JsonUtils.writeJsonToOutputStream(map,response);
                })
                .and()
                .exceptionHandling().accessDeniedHandler((request,response,exception)->{
                    response.setStatus(200);
                    Map<String,Object> map=new HashMap<>();
                    map.put("code",403);
                    map.put("error_url",request.getRequestURL());
                    map.put("message","访问被服务器拒绝,可能因为的您的权限不足,请联系管理员");
                    JsonUtils.writeJsonToOutputStream(map,response);
                })
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        var daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*")); // 允许的源
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 允许的方法
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // 允许的头
        config.setAllowCredentials(false); // 允许携带凭据（如 Cookie）
        config.setMaxAge(3600L); // 预检请求的缓存时间（单位：秒）
        return config;
    }
}
