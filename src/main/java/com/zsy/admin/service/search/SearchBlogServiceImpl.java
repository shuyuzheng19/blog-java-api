package com.zsy.admin.service.search;
import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.request.admin.GptRequest;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.response.search.SearchResponse;
import com.zsy.admin.utils.ChatGptUtils;
import com.zsy.admin.utils.HttpUtils;
import com.zsy.admin.vos.SearchBlog;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author 郑书宇
 * @create 2023/6/7 0:01
 * @desc
 */
@Service
public class SearchBlogServiceImpl implements SearchBlogService {

    private static final String BLOG_INDEX=Constants.ARTICLE_INDEX_NAME;

    @Resource
    private MedilsearchClient medilsearchClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void saveBlog(String json) {
        medilsearchClient.save(BLOG_INDEX,json);
    }

    @Override
    public void dropIndex() {
        medilsearchClient.dropIndex(BLOG_INDEX);
    }

    @Override
    public void deleteAllDocument() {
        medilsearchClient.deleteAllDocument(BLOG_INDEX);
    }

    @Override
    public void createIndex() {
        medilsearchClient.createIndex(BLOG_INDEX);
    }

    @Override
    public Set<String> getHotSearchKeyWord() {
        Set<String> set = redisTemplate.opsForZSet().reverseRangeByScore(RedisConstants.SEARCH_COUNT, 0, Integer.MAX_VALUE, 0, 10);
        return set;
    }

    @Override
    public PageInfo<SearchBlog> countSearch( String keyword,Integer page) {
        PageInfo<SearchBlog> search = search(page, keyword);
        if(search.getTotal()>0){
            redisTemplate.opsForZSet().incrementScore(RedisConstants.SEARCH_COUNT,keyword,1);
        }
        return search;
    }

    @Override
    public List<SearchBlog> similarBlog( String keyword, Integer id) {
        PageInfo<SearchBlog> search = search(1, keyword);
        return search.getData();
    }

    @Override
    public PageInfo<SearchBlog> search(int page, String keyword) {

        SearchResponse<SearchBlog> searchResponse = medilsearchClient.search(BLOG_INDEX,page,keyword,SearchBlog.class);

        PageInfo<SearchBlog> pageInfo=new PageInfo<>();

        pageInfo.setTotal(searchResponse.getEstimatedTotalHits());

        pageInfo.setPage(page);

        if(searchResponse==null) {
            pageInfo.setData(new ArrayList<>());
        }else{
            pageInfo.setData(searchResponse.getHits());
        }

        pageInfo.setSize(Constants.BLOG_PAGE_SIZE);

        return pageInfo;
    }

    public static void main(String[] args) throws IOException {
        String message= "你好,给我写一篇关于springboot的文章";
        ChatGptUtils chatGptUtils=new ChatGptUtils();

        Map<String,String> headers=new HashMap<>();

        headers.put("Content-Type","application/json;charset=utf-8");

        headers.put("host","ai.fakeopen.com");

        headers.put("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJzaHV5dXpoZW5nMTlAMTYzLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlfSwiaHR0cHM6Ly9hcGkub3BlbmFpLmNvbS9hdXRoIjp7InVzZXJfaWQiOiJ1c2VyLW56OG9sQUR3d1lZbG9yM3Bla1NMQVJJNiJ9LCJpc3MiOiJodHRwczovL2F1dGgwLm9wZW5haS5jb20vIiwic3ViIjoiYXV0aDB8NjM5MzI1M2I1NTU0OTQyMzQ4NWNkY2FjIiwiYXVkIjpbImh0dHBzOi8vYXBpLm9wZW5haS5jb20vdjEiLCJodHRwczovL29wZW5haS5vcGVuYWkuYXV0aDBhcHAuY29tL3VzZXJpbmZvIl0sImlhdCI6MTY4NjA2MTkwNiwiZXhwIjoxNjg3MjcxNTA2LCJhenAiOiJUZEpJY2JlMTZXb1RIdE45NW55eXdoNUU0eU9vNkl0RyIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgbW9kZWwucmVhZCBtb2RlbC5yZXF1ZXN0IG9yZ2FuaXphdGlvbi5yZWFkIG9yZ2FuaXphdGlvbi53cml0ZSJ9.qiF3EvwfA2P6TCNNv2T1dBh7GXrTgU3-GYNZtTWxt6wLnU_tWBdPx29Y1s543x9Z9BRyv-JXAR_jGLll-ffZprCiZx9lVKgx5BgodyhmCQc9vaJ9E_HzvHwJfPd9RqLflQhWZKcFRl-U2URFnjjMFgNPYctZ8LX9DCWgOCTXdQZyg1QK7oWa82QxE1qLrNh4FpZbRIudheGQl4Rep2X1nqinLCliCF7yCZle8W3o5xl-kYrGC6SdFoq8LwBQumDzRjUkBa7xTdYWsagksJIt2-eoZHlQPybkEfMlulvyqmJaPlKMJBEr0cEza6VXoY0Olq8Mb5CU1wsipWAs12RFnA");

        if(message.length()>2000){
            message=message.substring(0,2000);
        }

        String REQUEST_BODY= GptRequest.toJson(List.of(message),"text-davinci-002-render-sha-mobile");

        InputStream resultContent = HttpUtils.getPostUrlResultInputStream("https://ai.fakeopen.com/api/conversation", REQUEST_BODY, headers);

        int len=-1;

        byte[] buff = new byte[1024];

        while((len=resultContent.read(buff))!=-1){
            System.out.println(new String(buff,0,len));
        }

        resultContent.close();

    }
}
