package com.zsy.admin.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.request.ContactMeRequest;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 郑书宇
 * @create 2023/6/4 12:22
 * @desc
 */
@Service
public class ValidatorService {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private DefaultKaptcha defaultKaptcha;

    @Value("${spring.mail.username}")
    private String myEmail;

    @Value("${myEmail}")
    private String myEmail2;


    public void sendContactToMyMail(ContactMeRequest request) throws MessagingException {
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,true);
        String htmlContent = "<h3>%s</h3><p>对方名字:%s</p><p>对方邮箱:%s</p>留言内容:<p>%s</p>";
        String content = String.format(htmlContent,request.getSubject(),request.getName(),request.getEmail(),request.getContent());
        mimeMessageHelper.setTo(myEmail2);
        mimeMessageHelper.setSubject(request.getSubject());
        mimeMessageHelper.setText(content,true);
        mimeMessageHelper.setFrom(myEmail);

        try {
            mailSender.send(mimeMailMessage);
        } catch (MailException e) {
            throw GlobalException.builder().code(ResultCode.ERROR_CODE).message("发送失败").build();
        }
    }


    public boolean sendMessage(String toEmail,String subject, String content){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        simpleMailMessage.setFrom(myEmail);
        try{
            mailSender.send(simpleMailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void saveEmailCodeToRedis(String email,String code){
        stringRedisTemplate.opsForValue().set(RedisConstants.GET_EMAIL_CODE +email,code,2, TimeUnit.MINUTES);
    }

    public boolean verificationEmail(String email,String code){
        String result = stringRedisTemplate.opsForValue().get(RedisConstants.GET_EMAIL_CODE + email);
        return !StringUtils.isEmpty(result) && result.equals(code);
    }


    public String createCaptChaImage(String ip){

        String code = defaultKaptcha.createText();

        BufferedImage image = defaultKaptcha.createImage(code);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        try {
            ImageIO.write(image,"png",byteArrayOutputStream);
        } catch (IOException e) {
            throw GlobalException.builder().code(ResultCode.ERROR_CODE).message("验证码生成出错").build();
        }

        stringRedisTemplate.opsForValue().set(RedisConstants.CAPTCHA_CODE +ip,code,60,TimeUnit.SECONDS);

        String imageBase64 = Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());

        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageBase64;
    }
}
