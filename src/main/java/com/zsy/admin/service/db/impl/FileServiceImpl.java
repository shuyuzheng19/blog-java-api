package com.zsy.admin.service.db.impl;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.entity.FileInfo;
import com.zsy.admin.entity.FileMd5;
import com.zsy.admin.enums.UploadTypeEnum;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.repository.FileInfoRepository;
import com.zsy.admin.request.FileRequest;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.service.db.FileService;
import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.vos.FileVo;
import io.micrometer.common.util.StringUtils;
import jakarta.activation.MimetypesFileTypeMap;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 郑书宇
 * @create 2023/6/12 21:03
 * @desc
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileInfoRepository fileInfoRepository;

    @Value("${upload.path}")
    private String path;

    @Value("#{${upload.max.size.image}*1024*1024}")
    private long maxImageSize;

    @Value("#{${upload.max.size.file}*1024*1024}")
    private long maxFileSize;

    @Value("${upload.uri}")
    private String uri;

    @Override
    public PageInfo<FileVo> getFileInfo(boolean isPublic,FileRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), Constants.FILE_COUNT, Sort.by(Sort.Order.desc(request.getSort()))).previousOrFirst();
        Page<FileInfo> page = fileInfoRepository.findAll((root, query, build) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (isPublic) {
                predicates.add(build.equal(root.get("isPublic"), true));
            } else {
                predicates.add(build.equal(root.get("userId"), GlobalUtils.getCurrentUserId()));
            }
            if (!StringUtils.isEmpty(request.getKeyword())) {
                predicates.add(build.like(root.get("oldName"), "%" + request.getKeyword() + "%"));
            }
            return build.and(predicates.toArray(new Predicate[]{}));
        }, pageable);

        PageInfo<FileVo> pageInfo=new PageInfo<>();
        pageInfo.setPage(page.getNumber()+1);
        pageInfo.setSize(page.getSize());
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setData(page.getContent().stream().map(f->f.toVo()).collect(Collectors.toList()));
        return pageInfo;
    }

    @Override
    @Transactional
    public List<String> saveFile(boolean isPublic, MultipartFile[] files, UploadTypeEnum typeEnum) throws IOException {

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {

            String originalFilename = file.getOriginalFilename();

            boolean flag = typeEnum.equals(UploadTypeEnum.FILES);

            long fileSize = file.getSize();

            if(!flag) {
                if(!isImage(originalFilename)){
                    throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("这不是一张图片").build();
                }
                if(fileSize>maxImageSize){
                    throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("图片大小超出").build();
                }
            }else{
                if(fileSize>maxFileSize){
                    throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("文件大小超出").build();
                }
            }

            String md5 = GlobalUtils.getMd5(file.getBytes());

            String md5Url = fileInfoRepository.findMD5(md5);

            boolean exists=md5Url!=null;

            boolean flag2 = typeEnum.equals(UploadTypeEnum.AVATAR);

            if(exists && flag2) {
                urls.add(md5Url);
                continue;
            }

            String suffix = GlobalUtils.getFileNameSuffix(originalFilename);

            String fileName= UUID.randomUUID()+"."+suffix;

            String typeDir = typeEnum.getType();

            String filePath = path+"/"+typeDir+"/"+fileName;

            if(!exists){
                FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(filePath));
            }

            String url = null;

            if(exists) {
                url=md5Url;
            }else{
                url=uri+typeDir+"/"+fileName;
            }

            if(flag) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setUserId(GlobalUtils.getCurrentUserId());
                fileInfo.setOldName(originalFilename);
                fileInfo.setNewName(fileName);
                fileInfo.setDate(new Date());
                fileInfo.setSize(fileSize);
                fileInfo.setSuffix(suffix);
                fileInfo.setUrl(url);
                fileInfo.setAbsolutePath(filePath);
                fileInfo.setIsPublic(isPublic);
                fileInfo.setFileMd5(FileMd5.of(md5, url));
                if(!exists) {
                    FileInfo result = fileInfoRepository.save(fileInfo);
                    if (result.getId() == null) {
                        throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("添加文件失败").build();
                    }
                }
            }else{
                if(!exists) {
                    fileInfoRepository.saveMD5(md5, url);
                }
            }
            urls.add(url);
        }

        return urls;
    }

    public boolean isImage(String type) {
        // 获取文件的内容类型
        String contentType = new MimetypesFileTypeMap().getContentType(type);
        // 判断是否为图片类型
        return contentType != null && contentType.startsWith("image/");
    }
}
