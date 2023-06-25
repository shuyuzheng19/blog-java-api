package com.zsy.admin.service.search;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.request.SearchRequest;
import com.zsy.admin.response.search.SearchResponse;
import com.zsy.admin.utils.HttpUtils;
import com.zsy.admin.utils.JsonUtils;
import com.zsy.admin.vos.SearchBlog;
import lombok.Data;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author 郑书宇
 * @create 2023/6/12 0:03
 * @desc
 */
@Data
public class MedilsearchClient {
    private String API_HOST;

    private String API_KEY;

    private Map<String,String> map;

    public MedilsearchClient(String apiHost,String apiKey){
        this.API_HOST=apiHost;
        this.API_KEY=apiKey;
        map=Map.of("Authorization","Bearer "+API_KEY,"Content-Type","application/json;");
    }

    public void dropIndex(String indexName) {
        CloseableHttpClient httpClient = HttpClients.custom().build();

        HttpDelete httpDelete = new HttpDelete(API_HOST+"/indexes/"+indexName);

        for (String key : map.keySet()) {
            httpDelete.addHeader(key,map.get(key));
        }

        try {
            httpClient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllDocument(String indexName){
        CloseableHttpClient httpClient = HttpClients.custom().build();

        HttpDelete httpDelete = new HttpDelete(API_HOST+"/indexes/"+indexName+"/documents");

        for (String key : map.keySet()) {
            httpDelete.addHeader(key,map.get(key));
        }

        try {
            httpClient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createIndex(String indexName) {
        HttpUtils.getPostUrlResultContent(API_HOST+"/indexes/"+indexName,"{ \"uid\": \""+indexName+"\", \"primaryKey\": \"id\" }", map);
    }

    public void save(String indexName,String json){
        HttpUtils.getPostUrlResultContent(API_HOST + "/indexes/" + indexName+"/documents", json, map);
    }

    public <T> SearchResponse<T> search(String indexName, int page, String keyword,Class<T> tClass) {
        SearchRequest.SearchRequestBuilder builder = SearchRequest.builder()
                .q(keyword).attributesToHighlight(Arrays.asList("*")).offset((page-1)* Constants.BLOG_PAGE_SIZE).limit(Constants.BLOG_PAGE_SIZE)
                .showMatchesPosition(false).highlightPreTag(Constants.HIG_PRE)
                .highlightPostTag(Constants.HIG_SUFFIX);

        String resultContent = HttpUtils.getPostUrlResultContent(API_HOST+"/indexes/"+indexName+"/search", JsonUtils.objectToJson(builder.build()), map);

        SearchResponse<T> searchResponse = JsonUtils.jsonToObject(resultContent, SearchResponse.class);

        return searchResponse;
    }

    public static void main(String[] args) {
        MedilsearchClient medilsearchClient=new MedilsearchClient("http://192.168.25.147:7700","zhengshuyuzuishuaile8889966");
        SearchBlog searchBlog=new SearchBlog(1,"标题","描述");
        medilsearchClient.save("blogs2",JsonUtils.objectToJson(searchBlog));
    }
}
