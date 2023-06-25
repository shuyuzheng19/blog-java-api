package com.zsy.admin.repository;

import com.zsy.admin.entity.FileInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FileInfoRepository extends JpaRepository<FileInfo, Integer>, JpaSpecificationExecutor<FileInfo> {
    @Query("SELECT md5.url from FileMd5 md5 where md5.md5=:md5")
    String findMD5(String md5);

    @Query(nativeQuery = true,value = "insert into file_md5(md5,url) values(:md5,:url)")
    @Modifying
    @Transactional
    void saveMD5(String md5,String url);

//    @Query("select new com.zsy.admin.vos.FileInfoVo(file.id,file.oldName,file.fileMd5.url,file.fileMd5.md5,file.size,file.date) from FileInfo file where file.userId=:userId")
//    Page<FileInfoVo> findCurrentUserPageFile(Pageable pageable, Integer userId);
//
//    @Query("select new com.zsy.admin.vos.FileInfoVo(file.id,file.oldName,file.url,file.fileMd5.md5,file.size,file.date) from FileInfo file where file.isPublic=true")
//    Page<FileInfoVo> findPublicPageFile(Pageable pageable);
}
