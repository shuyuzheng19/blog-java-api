package com.zsy.admin.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 郑书宇
 * @create 2023/5/4 14:13
 * @desc
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAdviceController {

    @ExceptionHandler(GlobalException.class)
    public Map<String,Object> globalException(GlobalException exception, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        map.put("code",exception.getCode().getCode());
        map.put("message",exception.getMessage());
        map.put("url",request.getRequestURL());
        return map;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map fail(HttpServletRequest request, MethodArgumentNotValidException exception){
        BindingResult bindingResult=exception.getBindingResult();
        Map<String,Object> map=new HashMap();
        map.put("code",ResultCode.PARAMS_CODE.getCode());
        map.put("message",bindingResult.getFieldError().getDefaultMessage());
        map.put("error_url",request.getRequestURL());
        return map;
    }

    @ExceptionHandler(Exception.class)
    public Map error(HttpServletRequest request,Exception exception){
        Map<String,Object> map=new HashMap();
        map.put("code",500);
        map.put("message","啊哦 服务器开小差了.......");
        map.put("error_url",request.getRequestURL());
        log.error("服务器异常了==== ",exception);
        return map;
    }


}
