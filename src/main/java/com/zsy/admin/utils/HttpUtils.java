package com.zsy.admin.utils;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author 郑书宇
 * @create 2023/6/7 4:22
 * @desc
 */
public class HttpUtils {

    public static String getPostUrlResultContent(String url, String json, Map<String,String> headers){

        CloseableHttpClient httpClient = HttpClients.custom().build();

        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader(HttpHeaders.CONTENT_TYPE,"application/json;charset=utf-8");

        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

        httpPost.setEntity(entity);

        if(headers!=null){
            for (String header : headers.keySet()) {
                httpPost.addHeader(header, headers.get(header));
            }
        }

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static InputStream getPostUrlResultInputStream(String url, String json, Map<String,String> headers) throws IOException {

        CloseableHttpClient httpClient = HttpClients.custom().build();

        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader(HttpHeaders.CONTENT_TYPE,"application/json;charset=utf-8");

        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

        httpPost.setEntity(entity);

        if(headers!=null){
            for (String header : headers.keySet()) {
                httpPost.addHeader(header, headers.get(header));
            }
        }

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        return httpResponse.getEntity().getContent();
    }
}
