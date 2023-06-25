package com.zsy.admin.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author 郑书宇
 * @create 2023/6/8 11:46
 * @desc
 */
@Data
@Entity
@Table(name="file_md5")
public class FileMd5 {

    @Id
    @Column(name="md5",nullable = false,unique = true)
    private String md5;

    private String url;

    public static FileMd5 of(String md5,String url){
        FileMd5 fileMd5=new FileMd5();
        fileMd5.setMd5(md5);
        fileMd5.setUrl(url);
        return fileMd5;
    }

    public static FileMd5 of(String md5){
        return of(md5,null);
    }
}
