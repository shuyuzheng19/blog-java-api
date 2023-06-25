package com.zsy.admin.config.filter;

import com.zsy.admin.response.Result;
import com.zsy.admin.service.MyUserDetailService;
import com.zsy.admin.service.MyUserDetails;
import com.zsy.admin.utils.JsonUtils;
import com.zsy.admin.utils.JwtUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import java.io.IOException;

/**
 * @author 郑书宇
 * @create 2023/6/4 0:10
 * @desc
 */
public class JwtFilter extends BasicAuthenticationFilter {

    private final String TOKEN_PREFIX="Bearer ";

    private final MyUserDetailService userDetailsService;

    public JwtFilter(AuthenticationManager authenticationManager,MyUserDetailService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService=userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        response.setHeader("Access-Control-Allow-Origin", "*");

        response.setHeader("Access-Control-Allow-Methods", "*");

        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        if(request.getMethod().equals("OPTIONS")){

            response.setStatus(HttpStatus.NO_CONTENT.value());

            chain.doFilter(request,response);

            return;
        }

        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(tokenHeader==null||!tokenHeader.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }

        String token=tokenHeader.replace(TOKEN_PREFIX,"");

        var authenticate =getAuthentication(token);

        if(authenticate==null){
            JsonUtils.writeJsonToOutputStream(Result.fail(403,"身份验证失败,请重新登录"),response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        chain.doFilter(request,response);
    }

    private Authentication getAuthentication(String token) {

        String username = JwtUtils.parseTokenToUsername(token);

        if (username != null && userDetailsService.verifyToken(username,token)) {
            MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails.getUser(), userDetails.getPassword(), userDetails.getAuthorities());
        }

        return null;
    }

}
