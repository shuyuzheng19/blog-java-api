package com.zsy.admin.utils;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.entity.User;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.response.Result;
import com.zsy.admin.vos.BlogVo;
import io.lettuce.core.ScriptOutputType;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.UnicodeEscaper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 郑书宇
 * @create 2023/6/4 0:03
 * @desc
 */
public class GlobalUtils {

    public static String ofApiUrl(String path){
        return Constants.API_PREFIX+path;
    }

    public static String ofApiUrl(String type,String path){
        return Constants.API_PREFIX+"/"+type+path;
    }

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String generateRandomNumber() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }

    public static User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser;
    }

    //获取文件的md5
    public static String getMd5(byte[] bytes){
        return DigestUtils.md5DigestAsHex(bytes);
    }

    //获取文件的后缀名
    public static String getFileNameSuffix(String fileName){
        if(StringUtils.isEmpty(fileName)) return null;

        int lastIndexOf= fileName.lastIndexOf(".");

        if(lastIndexOf==-1) return null;

        return fileName.substring(lastIndexOf+1);
    }

    public static <T> PageInfo<T> getPageInfo(Page<T> page){
        PageInfo<T> pageInfo=new PageInfo<>();
        pageInfo.setPage(page.getNumber()+1);
        pageInfo.setSize(page.getSize());
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setData(page.getContent());
        return pageInfo;
    }

    public static String toUnicode(String str){

        // 将字符串转换为Unicode编码
        StringBuilder unicodeBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int codePoint = str.codePointAt(i);
            unicodeBuilder.append(String.format("\\u%04X", codePoint));
            if (Character.isSupplementaryCodePoint(codePoint)) {
                i++;
            }
        }
        String unicodeStr = unicodeBuilder.toString();
        return unicodeStr;
    }

    public static Integer getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser==null) {
            throw GlobalException.builder().code(ResultCode.NOT_FOUNT_CODE).message("你还未登录").build();
        }
        return currentUser.getId();
    }

    public static String dateToStr(Date date){
        try{
            return dateToStr(date.getTime()/1000);
        }catch (Exception e){
            return "未知";
        }
    }

    public static String formatFileSize(long fileSize) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        double kilobytes = fileSize / 1024.0;
        double megabytes = kilobytes / 1024.0;
        double gigabytes = megabytes / 1024.0;

        if (gigabytes >= 1) {
            return decimalFormat.format(gigabytes) + " GB";
        } else if (megabytes >= 1) {
            return decimalFormat.format(megabytes) + " MB";
        } else if (kilobytes >= 1) {
            return decimalFormat.format(kilobytes) + " KB";
        } else {
            return fileSize + " Bytes";
        }
    }


    public static String dateToStr(long unix){

        long now= new Date().getTime()/1000;

        long second=now-unix;

        String dateStr="";

        if(second<=60) {
            dateStr = "刚刚";
        }else if(second>60&&second<=60*60){
            dateStr = second/60 + "分钟前";
        }else if(second>60*60&&second<=60*60*24){
            dateStr = second / 60 / 60  + "小时前";
        }else if(second>60*60*24&&second<=60*60*24*30){
            dateStr = second / 60 / 60 / 24 + "天前";
        }else if(second>60*60*24*30&&second<=60*60*24*30*12){
            dateStr = second / 60 / 60 / 24 / 30 + "月前";
        }else{
            dateStr = second / 60 / 60 / 24 / (30*12) + "年前";
        }

        return dateStr;
    }

    public static String getSizeStr(double size) {
        if (size == 0) {
            return "0 B";
        }

        String sizeStr = String.valueOf((int) size);

        if (size < 1024) {
            return sizeStr + " BIT";
        } else if (size > Constants.KB && size < Constants.MB) {
            return String.format("%.2f", size / 1024) + " KB";
        } else if (size > Constants.MB && size < Constants.GB) {
            return String.format("%.2f", size / (1024 * 1024)) + " MB";
        } else if (size > Constants.GB && size < Constants.TB) {
            return String.format("%.2f", size / (1024 * 1024 * 1024)) + " GB";
        } else if (size > Constants.TB) {
            return String.format("%.2f", size / (1024 * 1024 * 1024 * 1024)) + " TB";
        } else {
            return "未知";
        }
    }

    public static String formatDateTime(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
}
