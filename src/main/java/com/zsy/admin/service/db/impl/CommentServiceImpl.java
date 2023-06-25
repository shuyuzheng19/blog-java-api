package com.zsy.admin.service.db.impl;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.constants.RedisConstants;
import com.zsy.admin.entity.Comment;
import com.zsy.admin.entity.User;
import com.zsy.admin.error.GlobalException;
import com.zsy.admin.error.ResultCode;
import com.zsy.admin.repository.CommentRepository;
import com.zsy.admin.request.CommentRequest;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.service.db.CommentService;
import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.vos.CommentUserVo;
import com.zsy.admin.vos.CommentVo;
import com.zsy.admin.vos.ReplyVo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 郑书宇
 * @create 2023/6/7 22:23
 * @desc
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentRepository commentRepository;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public CommentVo addComment(Comment comment) {
        comment.setUser(GlobalUtils.getCurrentUser());
        Long parentId = comment.getParentId();
        if(parentId!=null) {
           Comment parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> GlobalException.builder().code(ResultCode.FAIL_CODE).message("回复失败").build());
           parentComment.getReply().add(comment);
           parentComment.setReply(parentComment.getReply());
        }
        Comment result = commentRepository.save(comment);
        if(result==null){
            throw GlobalException.builder().code(ResultCode.FAIL_CODE).message("添加评论失败").build();
        }
        return result.toCommentVo();
    }

    @Override
    public void likeComment(Integer blogId,Integer id) {
        Integer userId = GlobalUtils.getCurrentUserId();

        final String KEY= String.format(RedisConstants.USER_LIKE_COMMENT,blogId,userId);

        boolean isLike = redisTemplate.opsForSet().isMember(KEY, id);
        final String KEY2= RedisConstants.USER_LIKE_COMMENT_COUNT;
        int add = 1;
        if(isLike) {
            add = -1;
            redisTemplate.opsForSet().remove(KEY,id);
        }else{
            redisTemplate.opsForSet().add(KEY,id);
        }

        Long increment = redisTemplate.opsForHash().increment(KEY2, String.valueOf(id), add);

        if (increment < 0) {
            redisTemplate.opsForHash().put(KEY2, String.valueOf(id), 0);
        }
    }

    @Override
    public CommentUserVo getCurrentUserCommentVo(Integer blogId) {
        User user = GlobalUtils.getCurrentUser();
        CommentUserVo commentUserVo = user.toCommentUserVo();
        commentUserVo.setLikeIds(getCurrentUserBlogLikeCommentId(blogId,user.getId()));
        return commentUserVo;
    }

    @Override
    public Set<Integer> getCurrentUserBlogLikeCommentId(Integer blogId,Integer userId) {
        final String KEY= String.format(RedisConstants.USER_LIKE_COMMENT,blogId,userId);
        Set<Integer> members = redisTemplate.opsForSet().members(KEY);
        return members;
    }

    @Override
    public PageInfo<CommentVo> getCommentPage(int page, Integer blogId) {
        Page<Comment> pageResult = commentRepository.findCommentByBlogId(
                PageRequest.of(page, Constants.COMMENT_COUNT, Sort.by(Sort.Order.desc("createTime"))).previousOrFirst(),
                blogId
        );
        PageInfo<CommentVo> pageInfo=new PageInfo<>();
        pageInfo.setPage(pageResult.getNumber()+1);
        pageInfo.setTotal(pageResult.getTotalElements());
        pageInfo.setSize(Constants.COMMENT_COUNT);
        List<String> counts = new ArrayList<>();
        List<Comment> comments = pageResult.getContent();
        comments.stream().forEach(comment->{
            List<Comment> reply = comment.getReply();
            if(reply.size()>0){
                List<String> ids = new ArrayList<>();
                reply.stream().forEach(c->ids.add(c.getId().toString()));
                List<Integer> commentLikeCount = getCommentLikeCount(ids);
                for (int i = 0; i < commentLikeCount.size(); i++) {
                    Integer ct = commentLikeCount.get(i);
                    if(ct!=null){
                        reply.get(i).setLikes(ct);
                    }
                }
                comment.setReply(reply);
            }
            counts.add(comment.getId().toString());
        });
        List<Integer> commentLikeCount = getCommentLikeCount(counts);
        List<CommentVo> commentVoList=new ArrayList<>();
        for (int i = 0; i < counts.size(); i++) {
            Comment comment = comments.get(i);
            Integer ct = commentLikeCount.get(i);
            if(ct!=null){
                comment.setLikes(ct);
            }
            commentVoList.add(comment.toCommentVo());
        }
        pageInfo.setData(commentVoList);
        return pageInfo;
    }

    @Override
    public List<Integer> getCommentLikeCount(List<String> ids) {
        final String KEY= RedisConstants.USER_LIKE_COMMENT_COUNT;
        try{
            List<Integer> count =redisTemplate.opsForHash().multiGet(KEY,ids);
            return count;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
