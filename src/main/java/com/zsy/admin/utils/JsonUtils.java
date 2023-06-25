package com.zsy.admin.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author 郑书宇
 * @create 2023/6/4 1:42
 * @desc
 */
public class JsonUtils {

    public static void writeJsonToOutputStream(Object object, HttpServletResponse response){
        try {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            new ObjectMapper().writeValue(writer,object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String objectToJson(Object object){
        if(Objects.isNull(object)){
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    
    public static <T> T jsonToObject(String json, Class<T> tClass){
        if(StringUtils.isEmpty(json)) return null;
        try {
            return new ObjectMapper().readValue(json,tClass);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
