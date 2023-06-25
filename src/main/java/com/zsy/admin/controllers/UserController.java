package com.zsy.admin.controllers;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.entity.User;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.request.ContactMeRequest;
import com.zsy.admin.request.admin.LoginRequest;
import com.zsy.admin.response.admin.LoginResponse;
import com.zsy.admin.service.ValidatorService;
import com.zsy.admin.service.db.UserService;
import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.request.UserRegisteredRequest;
import com.zsy.admin.response.Result;
import com.zsy.admin.utils.IpUtils;
import com.zsy.admin.utils.JsonUtils;
import com.zsy.admin.utils.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 郑书宇
 * @create 2023/6/4 12:02
 * @desc
 */
@RestController
@RequestMapping(Constants.API_PREFIX+"/user")
@RequiredArgsConstructor
public class UserController {

    private final ValidatorService validatorService;

    private final UserService userService;

    @PostMapping("/registered")
    public Result registeredUser(@Valid @RequestBody UserRegisteredRequest userRequest){
        String email = userRequest.getEmail();
        String code = userRequest.getCode();
        boolean flag = validatorService.verificationEmail(email, code);
        if(flag) {
            userService.registerUser(userRequest.toUser());
            return Result.success();
        }else{
            return Result.fail("验证码错误,请核对后输入");
        }
    }

    @PostMapping("/contact")
    public Result contactMe(@Valid @RequestBody ContactMeRequest request) throws MessagingException {
        validatorService.sendContactToMyMail(request);
        return Result.success();
    }

    @GetMapping("/auth/get")
    public Result getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return Result.success(currentUser.toUserVo());
    }

    @PostMapping("/admin_login")
    public Result adminLogin(@Valid @RequestBody LoginRequest request){
        return Result.success(null);
    }

    @GetMapping("/captcha")
    public Result createCaptChaImage(HttpServletRequest request){
        String ip = IpUtils.getIpAddress(request);
        return Result.success(validatorService.createCaptChaImage(ip));
    }

    @GetMapping("/auth/logout")
    public Result logout(){
        userService.logout();
        return Result.success();
    }

    @GetMapping("/send_mail")
    public Result sendEmail(String email){
        boolean validEmail = GlobalUtils.isValidEmail(email);
        if(!validEmail) return Result.fail(ResultCode.FAIL_CODE.getCode(),"这不是一个正确的邮箱格式");
        String code = GlobalUtils.generateRandomNumber();
        boolean flag = validatorService.sendMessage(email, "ZSY-BLOG验证码", code);
        if(flag) {
            validatorService.saveEmailCodeToRedis(email,code);
            return Result.success();
        }else{
            return Result.fail("发送邮箱失败");
        }
    }
}
