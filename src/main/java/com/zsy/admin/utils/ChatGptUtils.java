package com.zsy.admin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsy.admin.request.admin.GptRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郑书宇
 * @create 2023/6/10 10:32
 * @desc
 */
@Service
@Data
public class ChatGptUtils {

//    private static final String API_URL = "https://ai.fakeopen.com/api/conversation";

    private static final String API_URL="https://api.pawan.krd/backend-api/conversation";

    @Value("${gpt.token}")
    private String TOKEN;

    @Value("${gpt.token2}")
    private String TOKEN2;

    @Value("${gpt.model}")
    private String MODEL;

    public void resetToken(String token){
        this.TOKEN=token;
    }

    public String sendMessage(String message){
        Map<String,String> headers=new HashMap<>();

        headers.put("Content-Type","application/json;charset=utf-8");

        headers.put("host","ai.fakeopen.com");

        headers.put("Authorization","Bearer "+TOKEN);

        if(message.length()>2000){
            message=message.substring(0,2000);
        }

        String REQUEST_BODY=GptRequest.toJson(List.of(message),MODEL);

        String resultContent = HttpUtils.getPostUrlResultContent(API_URL, REQUEST_BODY, headers);

        System.out.println(resultContent);

        String[] split = resultContent.split("\n\n");

        String result =null;

        try{
            result=split[split.length-2].substring("data: ".length());
        }catch (Exception e){
            throw e;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(result);
        } catch (JsonProcessingException e) {
            return null;
        }

        String resultMessage = jsonNode.get("message").get("content").get("parts").get(0).asText();

        return resultMessage;
    }

    public SseEmitter chat(String message,boolean release) throws IOException {
        Map<String,String> headers=new HashMap<>();

        headers.put("Content-Type","application/json;charset=utf-8");

        headers.put("host","ai.fakeopen.com");

        if(!release) {
            headers.put("Authorization", "Bearer " + TOKEN);
        }else{
            headers.put("Authorization","Bearer "+   TOKEN2);
        }

        String REQUEST_BODY= GptRequest.toJson(List.of(message),MODEL);

        InputStream inputStream = HttpUtils.getPostUrlResultInputStream("https://ai.fakeopen.com/api/conversation", REQUEST_BODY, headers);

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        new Thread(() -> {
            try (BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {

                    String data= null;

                    try{
                        data=line.split("data: ")[1].trim();
                        emitter.send(data);
                    }catch (ArrayIndexOutOfBoundsException e){
                        continue;
                    }
                }
                emitter.complete(); // 完成事件流发送
            } catch (Exception e) {
                emitter.completeWithError(e); // 发生错误时，完成事件流发送
            }
        }).start();
        return emitter;
    }

    public static void main(String[] args) {
        ChatGptUtils chatGptUtils=new ChatGptUtils();
        chatGptUtils.setTOKEN("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJzaHV5dXpoZW5nMTlAMTYzLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlfSwiaHR0cHM6Ly9hcGkub3BlbmFpLmNvbS9hdXRoIjp7InVzZXJfaWQiOiJ1c2VyLW56OG9sQUR3d1lZbG9yM3Bla1NMQVJJNiJ9LCJpc3MiOiJodHRwczovL2F1dGgwLm9wZW5haS5jb20vIiwic3ViIjoiYXV0aDB8NjM5MzI1M2I1NTU0OTQyMzQ4NWNkY2FjIiwiYXVkIjpbImh0dHBzOi8vYXBpLm9wZW5haS5jb20vdjEiLCJodHRwczovL29wZW5haS5vcGVuYWkuYXV0aDBhcHAuY29tL3VzZXJpbmZvIl0sImlhdCI6MTY4NjA2MTkwNiwiZXhwIjoxNjg3MjcxNTA2LCJhenAiOiJUZEpJY2JlMTZXb1RIdE45NW55eXdoNUU0eU9vNkl0RyIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgbW9kZWwucmVhZCBtb2RlbC5yZXF1ZXN0IG9yZ2FuaXphdGlvbi5yZWFkIG9yZ2FuaXphdGlvbi53cml0ZSJ9.qiF3EvwfA2P6TCNNv2T1dBh7GXrTgU3-GYNZtTWxt6wLnU_tWBdPx29Y1s543x9Z9BRyv-JXAR_jGLll-ffZprCiZx9lVKgx5BgodyhmCQc9vaJ9E_HzvHwJfPd9RqLflQhWZKcFRl-U2URFnjjMFgNPYctZ8LX9DCWgOCTXdQZyg1QK7oWa82QxE1qLrNh4FpZbRIudheGQl4Rep2X1nqinLCliCF7yCZle8W3o5xl-kYrGC6SdFoq8LwBQumDzRjUkBa7xTdYWsagksJIt2-eoZHlQPybkEfMlulvyqmJaPlKMJBEr0cEza6VXoY0Olq8Mb5CU1wsipWAs12RFnA");
        chatGptUtils.setMODEL("text-davinci-002-render-sha-mobile");
        String message = chatGptUtils.sendMessage("你好");
        System.out.println(message);
    }

}
