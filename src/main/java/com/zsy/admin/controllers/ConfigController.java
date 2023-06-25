package com.zsy.admin.controllers;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.response.BlogInfo;
import com.zsy.admin.response.Result;
import com.zsy.admin.service.ConfigService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 郑书宇
 * @create 2023/6/5 20:59
 * @desc
 */
@RestController
@RequestMapping(Constants.API_PREFIX+"/config")
public class ConfigController {

    @Resource
    private ConfigService configService;

    @GetMapping
    public Result getConfig(){
        return Result.success(configService.getConfig());
    }
}
