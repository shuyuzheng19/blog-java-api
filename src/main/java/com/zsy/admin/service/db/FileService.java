package com.zsy.admin.service.db;

import com.zsy.admin.enums.UploadTypeEnum;
import com.zsy.admin.request.FileRequest;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.vos.FileVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/12 21:02
 * @desc
 */
public interface FileService {
    //获取文件
    PageInfo<FileVo> getFileInfo(boolean isPublic,FileRequest request);

    //上传文件
    List<String> saveFile(boolean isPublic, MultipartFile[] file, UploadTypeEnum typeEnum) throws IOException;
}
