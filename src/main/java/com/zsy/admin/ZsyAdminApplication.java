package com.zsy.admin;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.zsy.admin.service.search.MedilsearchClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

/**
 * @author 郑书宇
 * @create 2023/6/3 22:18
 * @desc
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class ZsyAdminApplication implements WebMvcConfigurer {

//    @Value("${upload.path}")
//    private String path;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("file:"+path);
//    }

    public static void main(String[] args) {
        SpringApplication.run(ZsyAdminApplication.class,args);
    }

    @Bean
    public DefaultKaptcha createDefaultCaptCha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 图片边框
        properties.setProperty("kaptcha.border", "yes");
        // 边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        // 图片宽
        properties.setProperty("kaptcha.image.width", "100");
        // 图片高
        properties.setProperty("kaptcha.image.height", "40");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "33");
        // 验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 字体间隔
        properties.setProperty("kaptcha.textproducer.char.space", "4");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Value("${meilisearch.apiHost}")
    private String hostUrl;

    @Value("${meilisearch.apiKey}")
    private String apiKey;

    @Bean
    public MedilsearchClient medilsearchClient(){
        return new MedilsearchClient(hostUrl,apiKey);
    }


}
