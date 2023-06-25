package com.zsy.admin.controllers;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.enums.UploadTypeEnum;
import com.zsy.admin.request.FileRequest;
import com.zsy.admin.response.Result;
import com.zsy.admin.service.db.FileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/8 11:31
 * @desc
 */
@RestController
@RequestMapping(Constants.API_PREFIX+"/file")
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload/avatar")
    public Result uploadAvatar(@RequestParam("file")MultipartFile[] files) throws IOException {
        if(files==null || files.length==0){
            return Result.fail("你还没有选择文件");
        }
        List<String> urls = fileService.saveFile(false, files, UploadTypeEnum.AVATAR);
        return Result.success(urls.get(0));
    }

    @PostMapping("/upload/auth/image")
    public Result uploadImage(@RequestParam("file")MultipartFile[] files) throws IOException {
        if(files==null || files.length==0){
            return Result.fail("你还没有选择文件");
        }
        List<String> urls = fileService.saveFile(false, files, UploadTypeEnum.IMAGES);
        return Result.success(urls);
    }

    @PostMapping("/upload/admin/file")
    public Result uploadFile(boolean isPublic,@RequestParam("files")MultipartFile[] files) throws IOException {
        if(files==null || files.length==0){
            return Result.fail("你还没有选择文件");
        }
        List<String> urls = fileService.saveFile(isPublic, files, UploadTypeEnum.FILES);
        return Result.success(urls);
    }

    @GetMapping("/admin/current")
    public Result gerCurrentUserFile(@ModelAttribute FileRequest fileRequest){
        return Result.success(fileService.getFileInfo(false,fileRequest));
    }

    @GetMapping("/public")
    public Result getPublicFile(@ModelAttribute FileRequest fileRequest){
        return Result.success(fileService.getFileInfo(true,fileRequest));
    }




}
